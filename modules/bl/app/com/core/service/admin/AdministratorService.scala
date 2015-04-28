package com.core.service.admin

import java.util.{List => JList}

import com.core.dom.admin.{AccessPermission, Administrator}
import org.springframework.stereotype.Service

@Service
class AdministratorService {

  def hasPermission(permission: AccessPermission)(implicit me: Administrator): Boolean = {
    //TODO
    true
  }

}