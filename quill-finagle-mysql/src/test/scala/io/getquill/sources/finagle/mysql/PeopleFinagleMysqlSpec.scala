package io.getquill.sources.finagle.mysql

import com.twitter.util.Await
import com.twitter.util.Future

import io.getquill._
import io.getquill.sources.sql.PeopleSpec

class PeopleFinagleMysqlSpec extends PeopleSpec {

  def await[T](future: Future[T]) = Await.result(future)

  override def beforeAll =
    await {
      testDB.transaction { transactional =>
        for {
          _ <- transactional.run(query[Couple].delete)
          _ <- transactional.run(query[Person].filter(_.age > 0).delete)
          _ <- transactional.run(peopleInsert)(peopleEntries)
          _ <- transactional.run(couplesInsert)(couplesEntries)
        } yield {}
      }
    }

  "Example 1 - differences" in {
    await(testDB.run(`Ex 1 differences`)) mustEqual `Ex 1 expected result`
  }

  "Example 2 - range simple" in {
    await(testDB.run(`Ex 2 rangeSimple`)(`Ex 2 param 1`, `Ex 2 param 2`)) mustEqual `Ex 2 expected result`
  }

  "Examples 3 - satisfies" in {
    await(testDB.run(`Ex 3 satisfies`)) mustEqual `Ex 3 expected result`
  }

  "Examples 4 - satisfies" in {
    await(testDB.run(`Ex 4 satisfies`)) mustEqual `Ex 4 expected result`
  }

  "Example 5 - compose" in {
    await(testDB.run(`Ex 5 compose`)(`Ex 5 param 1`, `Ex 5 param 2`)) mustEqual `Ex 5 expected result`
  }

  "Example 6 - predicate 0" in {
    await(testDB.run(satisfies(eval(`Ex 6 predicate`)))) mustEqual `Ex 6 expected result`
  }

  "Example 7 - predicate 1" in {
    await(testDB.run(satisfies(eval(`Ex 7 predicate`)))) mustEqual `Ex 7 expected result`
  }

  "Example 8 - contains empty" in {
    await(testDB.run(`Ex 8 and 9 contains`)(`Ex 8 param`)) mustEqual `Ex 8 expected result`
  }

  "Example 9 - contains non empty" in {
    await(testDB.run(`Ex 8 and 9 contains`)(`Ex 9 param`)) mustEqual `Ex 9 expected result`
  }
}