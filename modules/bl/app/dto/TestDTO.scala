package dto

import com.core.service.admin.AdministratorService
import org.springframework.beans.factory.annotation.Autowired
import play.api.libs.json.{Json, Writes}

case class TestDTO()

object TestWrites {
  @Autowired
  var service: AdministratorService = _

  implicit val testWrites: Writes[TestDTO] = Writes {
    (result: TestDTO) => {
      Json.obj(
        "test.service" -> Json.toJson(service.getString("Hello"))
      )


    }
  }

  def getInstance() = this
}