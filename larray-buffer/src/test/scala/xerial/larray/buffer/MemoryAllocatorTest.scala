/*--------------------------------------------------------------------------
 *  Copyright 2013 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
//
// MemoryAllocatorTest.scala
// Since: 2013/03/22 12:02
//
//--------------------------------------

package xerial.larray.buffer

import xerial.larray.LArraySpec
import scala.collection.parallel.CollectionConverters._

/**
  * @author
  *   Taro L. Saito
  */
class MemoryAllocatorTest extends LArraySpec {
  test("ConcurrentMemoryAllocator") {
    test("perform better than the default heap allocator") {

      val N = 1000
      val B = 64 * 1024

      val t = time("alloc", repeat = 5) {
        block("concurrent") {
          val l = for (i <- (0 until N).par) yield {
            val a = new LBuffer(B)
            a(B - 1) = 1.toByte
            a
          }
          l.foreach(_.release)
        }

        block("Array") {
          val l = for (i <- (0 until N).par) yield {
            val a = new Array[Int](B)
            a(B - 1) = 1
            a
          }
        }
      }
      t("concurrent") < t("Array") shouldBe true
    }
  }
}
