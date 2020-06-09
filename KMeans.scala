import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object KMeans {
  type Point = (Double,Double)

  var centroids: Array[Point] = Array[Point]()
  var points: Array[Point] = Array[Point]()



  def main(args: Array[ String ]) {
    /* ... */
    val cong = new SparkConf().setAppName("KMeans")
    val sc= new SparkContext(cong)



    centroids = sc.textFile(args(1)).map(line => (line.split(",")(0).toDouble,line.split(",")(1).toDouble)).collect()
    /* read initial centroids from centroids.txt */
    points=sc.textFile(args(0)).map(line => (line.split(",")(0).toDouble,line.split(",")(1).toDouble)).collect()
    def distance(p1: Point) ={
      var eucld:Double=0.0
      var min_distance:Double=15000.0
      var c:Point=new Point(0,0)
      for(p <- 0 to centroids.length-1){
        eucld=Math.sqrt(Math.pow(Math.abs(p1._1-centroids(p)._1),2)+Math.pow(Math.abs(p1._2-centroids(p)._2),2))
        if(eucld<min_distance){ //using the minimum distance condition here only , just like we did before(assignment 1)
          min_distance=eucld
          c=centroids(p)
        }
      }
      (c,p1)

    }
    def centroids_new(pt:Array[((Double,Double),(Double,Double))]):(Double,Double) ={
      var x:Double=0.0
      var y:Double=0.0
      var c_new:Point=new Point(0,0)
      var count=pt.length
      for(p <- 0 to pt.length-1){
        x=x + pt(p)._2._1
        y=y + pt(p)._2._2
      }
      x=x/count
      y=y/count
      c_new=(x,y)
      c_new
    }

    for ( i <- 1 to 5 )
        centroids =points.map(pt1 => distance(pt1)).groupBy(pt2 =>pt2._1).map(pt3 => (centroids_new(pt3._2)) ).toArray
          /* find new centroids using KMeans */

    centroids.foreach(println)
    sc.stop()
  }
}
