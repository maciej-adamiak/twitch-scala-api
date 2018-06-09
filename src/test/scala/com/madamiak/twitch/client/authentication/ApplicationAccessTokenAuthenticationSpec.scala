package com.madamiak.twitch.client.authentication

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse, StatusCodes }
import akka.stream.ActorMaterializer
import com.madamiak.twitch.client.TwitchAPIException
import org.scalatest.{ AsyncWordSpec, Matchers }

import scala.concurrent.{ ExecutionContext, Future }

class ApplicationAccessTokenAuthenticationSpec extends AsyncWordSpec with Matchers {

  implicit val system: ActorSystem             = ActorSystem("test-actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  class TestAuthentication(
      implicit val system: ActorSystem,
      implicit val context: ExecutionContext,
      implicit val materializer: ActorMaterializer
  ) {
    this: Authentication =>
  }

  private val sut = new TestAuthentication() with ApplicationAccessTokenAuthentication

  "oauth authentication trait" which {

    "processes an authentication token" should {

      "extract it" when {

        "working with a valid request" in {
          val response = HttpResponse().withEntity(
            HttpEntity(
              ContentTypes.`application/json`,
              """
              |{
              |    "access_token": "yf9352bgpbzo6mhuytwybx3gg7arrq",
              |    "expires_in": 5363611,
              |    "scope": [
              |        "bits:read",
              |        "clips:edit"
              |    ]
              |}
            """.stripMargin
            )
          )

          sut.extractToken(response).map(_ shouldEqual "yf9352bgpbzo6mhuytwybx3gg7arrq")
        }
      }

      "fail with an exception" when {

        "id endpoint respond with an error status code" in {
          val response = HttpResponse().withStatus(StatusCodes.Unauthorized)

          recoverToSucceededIf[TwitchAPIException](
            sut.extractToken(response)
          )
        }
      }
    }

    "prepares an OAuth token request" should {

      "construct token request Uri" in {
        sut.tokenRequest
          .getUri()
          .toString shouldEqual "https://id.twitch.tv/oauth2/token?client_id=1234abcd&client_secret=1234secret&grant_type=client_credentials&scope=scopeA+scopeB+scopeC"
      }
    }

    "enables recovery in case of token expiration or revoke" should {

      "retry" when {

        "unauthorized" in {
          val counter = new AtomicInteger()
          sut
            .recoverWhenUnauthorized {
              if (counter.getAndIncrement() == 0) {
                Future.successful(HttpResponse().withStatus(StatusCodes.Unauthorized))
              } else {
                Future.successful(HttpResponse().withStatus(StatusCodes.OK))
              }
            }
            .map(_.status shouldEqual StatusCodes.OK)
        }

        "permanently unauthorized" in {
          sut
            .recoverWhenUnauthorized {
              Future.successful(HttpResponse().withStatus(StatusCodes.Unauthorized))
            }
            .map(_.status shouldEqual StatusCodes.Unauthorized)
        }

        "unauthorized and permanently failing" in {
          val counter = new AtomicInteger()
          sut
            .recoverWhenUnauthorized {
              if (counter.getAndIncrement() == 0) {
                Future.successful(HttpResponse().withStatus(StatusCodes.Unauthorized))
              } else {
                Future.successful(HttpResponse().withStatus(StatusCodes.InternalServerError))
              }
            }
            .map(_.status shouldEqual StatusCodes.InternalServerError)
        }
      }

      "not retry" when {

        "request was successful" in {
          sut
            .recoverWhenUnauthorized {
              Future.successful(HttpResponse().withStatus(StatusCodes.OK))
            }
            .map(_.status shouldEqual StatusCodes.OK)
        }

        "request failed" in {
          sut
            .recoverWhenUnauthorized {
              Future.successful(HttpResponse().withStatus(StatusCodes.InternalServerError))
            }
            .map(_.status shouldEqual StatusCodes.InternalServerError)
        }
      }
    }
  }
}
