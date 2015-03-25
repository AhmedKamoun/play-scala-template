package com.core.dal.queryDSL

import javax.persistence.{EntityManager, PersistenceContext}

import com.mysema.query.jpa.impl.JPAQuery


abstract class QueryDslRepository {
  @PersistenceContext
  private var em: EntityManager = _

  def query() = new JPAQuery(em)
}
