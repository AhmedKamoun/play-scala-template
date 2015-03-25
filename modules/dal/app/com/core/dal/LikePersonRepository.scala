package com.core.dal

import com.core.dom.LikePerson
import com.core.dom.person.Man
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait LikePersonRepository extends JpaRepository[LikePerson, String] {

  @Query("SELECT ls FROM Man a JOIN a.likes ls  WHERE a = :man AND ls.description = :des")
  def findByManByDescription(@Param("man") man: Man, @Param("des") des: String): java.util.List[LikePerson]
}
