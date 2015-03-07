package repositories.person

import entity.person.User
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait UserRepository extends JpaRepository[User, String] {
  @Query("select t FROM User t where email = :email")
  def findByEmail(@Param("email") email: String): User


}
