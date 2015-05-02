package com.core.dal.admin

import com.core.dom.admin.AccessPermission
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait AccessPermissionRepository extends JpaRepository[AccessPermission, String] {
  @Query("SELECT ap FROM AccessPermission ap  WHERE identifier = :identifier ")
  def findByIdentifier(@Param("identifier") identifier: String): AccessPermission


}
