package com.core.dal.admin

import com.core.dom.admin.{AccessPermission, Administrator}
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait AdministratorRepository extends JpaRepository[Administrator, String] {

  @Query("SELECT ca FROM Administrator ca  WHERE email = :email ")
  def findByEmail(@Param("email") id: String): Administrator

  @Query("SELECT ca FROM Administrator ca  WHERE id = :id AND ca.group IN (SELECT DISTINCT gap.group FROM GroupAdminPermission gap WHERE permission = :permission )")
  def findOneByPermission(@Param("id") id: String, @Param("permission") permission: AccessPermission): Administrator

  @Query("SELECT ca FROM Administrator ca  WHERE id = :id AND ca.group IN (SELECT DISTINCT gap.group FROM GroupAdminPermission gap WHERE permission = :permission AND  isAdmin = :isAdmin  )")
  def findOneByPermissionIsAdmin(@Param("id") id: String, @Param("permission") permission: AccessPermission, @Param("isAdmin") isAdmin: Boolean): Administrator


}
