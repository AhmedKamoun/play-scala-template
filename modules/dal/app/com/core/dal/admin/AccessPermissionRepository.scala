package com.core.dal.admin

import com.core.dom.admin.AccessPermission
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait AccessPermissionRepository extends JpaRepository[AccessPermission, String] {
  @Query("SELECT ap FROM AccessPermission ap  WHERE value = :permission ")
  def findByValue(@Param("permission") value: String): AccessPermission


}
