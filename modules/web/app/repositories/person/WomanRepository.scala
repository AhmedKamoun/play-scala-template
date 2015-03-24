package repositories.person

import entity.person.Woman
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait WomanRepository extends JpaRepository[Woman, String] {

}
