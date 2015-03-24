package repositories.queryDSL

import java.util.{List => JList}

import com.core.dom.person.{Man, Person}
import com.mysema.query.types.expr.BooleanExpression
import com.mysema.query.types.path._
import com.mysema.query.{BooleanBuilder, Tuple}
import org.joda.time.{DateTime, DateTimeConstants}
import org.springframework.stereotype.Repository
import play.api.libs.json.{JsArray, JsBoolean, Json}

import scala.collection.JavaConversions._

@Repository
class ManQueryDsl extends QueryDslRepository {

  private val man: PathBuilder[Man] = new PathBuilder[Man](classOf[Man], "man")
  private val createdOn = man.getDateTime("createdOn", classOf[DateTime])

  private val person: PathBuilder[Person] = new PathBuilder[Person](classOf[Person], "person")
  private val name = person.getString("name")

  //SIMPLE QUERY
  def allMenLike(searchTerm: String): List[Man] = {
    query.from(man).where(name.like("%" + searchTerm + "%")).list(man).toList

  }


  //SIMPLE BOOLEAN EXPRESSION
  def nameLike(term: String): BooleanExpression = {
    name.like("%" + term + "%")

  }

  def createdBeforeMonth(month: Integer) = {
    createdOn.month().lt(month)
  }

  //COMPLEX COMPOSITE QUERY
  def compositeQuery(): List[Man] = {
    var builder: BooleanBuilder = new BooleanBuilder()
    builder.or(nameLike("Zo"))
    builder.and(createdBeforeMonth(new Integer(DateTimeConstants.AUGUST)))

    query.from(man)
      //.where(builder)
      .groupBy(createdOn.yearWeek(),
        createdOn.year())
      .list(man).toList

  }

  def personList(): JsArray = {
    val list = query.from(person)
      .list(name, nameLike("kam"), person.count())
      .toList

    var array = Json.arr()
    var personElem = Json.obj()

    for (row: Tuple <- list) {
      personElem = personElem + ("name" -> Json.toJson(row.get(name)))
      personElem = personElem + ("like" -> JsBoolean(row.get(nameLike("kam"))))
      //  personElem = personElem + ("count" -> Json.toJson(person.count().longValue()))

      array = array :+ personElem
    }
    array
  }


  /*
   *
   * functions below are used for achievment test
   */


  // Meilleure semaine par année selon le nombre des men créés
  def bestYearWeek(): java.lang.Long = {
    query
      .from(man)
      .orderBy(man.count().desc())
      .groupBy(createdOn.year(), createdOn.yearWeek())
      .singleResult(man.count())

  }

  // Meilleure semaine selon le nombre des personnes créés
  def bestYearWeek_Person(): java.lang.Long = {
    query
      .from(person)
      .orderBy(person.count().desc())
      .groupBy(person.getDateTime("createdOn", classOf[DateTime]).year(), person.getDateTime("createdOn", classOf[DateTime]).yearWeek())
      .singleResult(person.count())

  }


  // Meilleur jour selon le nombre des personnes créés
  def bestDayOfYear(): java.lang.Long = {
    query
      .from(man)
      .orderBy(man.count().desc())
      .groupBy(createdOn.year(),
        createdOn.yearWeek(),
        createdOn.dayOfWeek()
      )
      .singleResult(man.count())

  }

  // Pendant un intervalle des heures
  def atPeriodOfHour(start_from_hour: java.lang.Long, end_at_hour: java.lang.Long): java.lang.Long = {
    query
      .from(man)
      .where(createdOn.hour().gt(start_from_hour).and(createdOn.hour().lt(end_at_hour)))
      .singleResult(man.count())

  }

  // durant les x derniers jours => specified number of days in a row
  def lastXDays(periode: Int): java.lang.Long = {
    query
      .from(man)
      .where(createdOn.gt((new DateTime()).minusDays(periode)))
      .singleResult(man.count())

  }

}
