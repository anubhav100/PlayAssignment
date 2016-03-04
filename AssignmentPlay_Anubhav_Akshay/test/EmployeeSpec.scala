
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._


@RunWith(classOf[JUnitRunner])
class EmployeeSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beSome.which(status(_) == NOT_FOUND)
    }

    "render the employeelisting page" in new WithApplication {
      val employeelistingpage = route(FakeRequest(GET, "/list")).get
      status(employeelistingpage) must equalTo(200)
    }
    "render to add more employees for faliure " in new WithApplication {

      val home = route(FakeRequest(POST, "//addEmployee")).get

      status(home) must equalTo(404)
    }
    "render to add more employees f " in new WithApplication {

      val home = route(FakeRequest(GET, "/addEmployee")).get

      status(home) must equalTo(200)
    }
    "check for submitting of add more employees form " in new WithApplication {

      val request = route(FakeRequest(POST, "/auth").withFormUrlEncodedBody("name" -> "anubhav", "address" -> "ggn", "dob" -> "1999-04-9", "joiningDate" -> "1997-05-23", "designation" -> "sotrainee")).get

      status(request) must equalTo(303)
    }
    "search employee with valid name" in new WithApplication {
      val home = route(FakeRequest(POST, "/search_auth  ").withFormUrlEncodedBody("name" -> "anubhav")).get

      status(home) must equalTo(404)

    }
  }
}