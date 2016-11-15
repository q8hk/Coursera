import java.io._
import java.util.{StringTokenizer}
import math._

object ClosestPair {

  class Point(var x: Int, var y: Int) {
    override def toString: String = "Point(" + x + ", " + y + ")"
  }

  def getDistance(p1: Point, p2: Point): Double =
    sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2))

  def closestPairsBrutForce(subPoints: List[Point]): Double = {
    var minDistance = Double.PositiveInfinity
    for (i <- 0 until subPoints.length; j <- i + 1 until subPoints.length) {
      val distance = getDistance(subPoints(i), subPoints(j))
      if (distance < minDistance)
        minDistance = distance
    }
    minDistance
  }

  def closestPairs(points: List[Point]): Double = {
    def closestPairsIter(yP: List[Point]): Double = {
      if (yP.length <= 3)
        closestPairsBrutForce(yP)
      else {
        val mid = yP.length / 2
        val xm = yP(mid)
        val (xL, xR) = yP splitAt mid
        val dL = closestPairsIter(xL)
        val dR = closestPairsIter(xR)
        val dmin = min(dL, dR)

        var m: Int = 0
        val yS = Array.ofDim[Point](yP.length)
        for (i <- yP.indices) {
          if (abs(yP(i).x - xm.x) < dmin) {
            yS(m) = yP(i)
            m = m + 1
          }
        }

        var closest = dmin
        for (i <- 0 until m) {
          var k = i + 1
          while (k < m && (yS(k).y - yS(i).y < dmin)) {
            val distance = getDistance(yS(k), yS(i))
            if (distance < closest)
              closest = distance
            k = k + 1
          }
        }

        closest
      }
    }
    val res = closestPairsIter(points.sortWith((t1, t2) => t1.y < t2.y))
    BigDecimal(res).setScale(6, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  def main(args: Array[String]): Unit = {
    new Thread(null, new Runnable() {
      def run() {
        try
          runClosest()

        catch {
          case e: IOException => {
          }
        }
      }
    }, "1", 1 << 26).start()
  }

  def runClosest() = {
    val scanner: FastScanner = new FastScanner(System.in)
    val n: Int = scanner.nextInt

    def buildStartsEnds(n: Int, l: List[Point]): List[Point] =
      if (n == 0) l
      else buildStartsEnds(n - 1, new Point(scanner.nextInt, scanner.nextInt) :: l)

    val points = buildStartsEnds(n, Nil)

    println(closestPairs(points))
  }

  class FastScanner(val stream: InputStream) {

    var br: BufferedReader = null
    var st: StringTokenizer = null

    try
      br = new BufferedReader(new InputStreamReader(stream))

    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }

    def next: String = {
      while (st == null || !st.hasMoreTokens)
        try
          st = new StringTokenizer(br.readLine)

        catch {
          case e: IOException => {
            e.printStackTrace()
          }
        }
      st.nextToken
    }

    def nextInt: Int = next.toInt
  }
}