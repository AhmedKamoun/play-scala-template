package repositories.person

import dto.PersonDTO
import entity.person.Person
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.stereotype.Repository


@Repository
trait PersonRepository extends JpaRepository[Person, String] {
  @Query("SELECT  new dto.PersonDTO( a, '' )  FROM Person a )")
  def findPersonDTO(): java.util.List[PersonDTO]

  @Query("SELECT  new dto.PersonDTO( a, '' )  FROM Person a WHERE a.job.private_job = true)")
  def findPersonsInPrivateSector(): java.util.List[PersonDTO]

}
