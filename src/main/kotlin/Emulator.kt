
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class Emulator {

    var aimNum:Int = 100
    var nowNum:Int = 0
    private val locX = arrayOf(663, 430, 310, 160, 955, 536, 206, 576, 902, 920, 879, 547, 1014)
    private val locY = arrayOf(332, 2089, 448, 419, 206, 2074, 1659, 1831, 1961, 1834, 1688, 1689, 118)
    private val locAlipayX = arrayOf(145, 995, 167, 502, 144, 502, 156, 942, 774, 922,922,525,525,599,599,975)
    private val locAlipayY = arrayOf(300, 132, 331, 2081, 2088, 2081, 1619, 2003, 2079, 1606,1606,1735,1735,1927,1927,133)
    private val pictureX = arrayOf(155, 411, 701, 942, 134,424,710)
    private val pictureY = arrayOf(357, 327, 350, 317, 580,594,586)
    var ipAddress = "101"
    private val adbHome = "adb" //"/home/mark/data/sdk/platform-tools/adb"
    private val realDevice = "beff28c7"
    private val ipDevice = "192.168.0.${ipAddress}:5555"
    private val device = realDevice

    private fun clink(x:Int,y:Int){
        val cmd = arrayOf(adbHome, "-s",device,"shell", "input", "tap", x.toString(), y.toString())
        //println("Execute cmd: input tap $x $y")
        val p = Runtime.getRuntime().exec(cmd)
//        val br = BufferedReader(InputStreamReader(p.inputStream))
//        var s:String?
//        while (br.readLine().also { s = it } != null)
//            println("line: $s")
        p.waitFor()
        //println("exit: " + p.exitValue())
        p.destroy()
    }

    fun test(i:Int){
        clink(pictureX[i],pictureY[i])
    }

    fun alipay(){
        val picStep = 2
        for (j in 3..3){
            nowNum = 0
            while (nowNum<aimNum){
                println("---------------------------------------------------")
                val sdf = SimpleDateFormat() // 格式化时间
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a") // a为am/pm的标记
                val date = Date() // 获取当前时间
                println("Time：" + sdf.format(date)) // 输出已经格式化的现在时间（24小时制）
                nowNum+=1
                println("Pic: $j   Number: $nowNum")
                print("\n")
                for (i in locAlipayX.indices){
                    if (i == picStep){
                        clink(pictureX[j],pictureY[j])
                    }else{
                        clink(locAlipayX[i],locAlipayY[i])
                    }
                    when(i){
                        0,1 -> Thread.sleep(500)
                        2 -> Thread.sleep(1000)
                        3,4,5,6 -> Thread.sleep(100)
                        7 -> Thread.sleep(2500)
                        8 -> Thread.sleep(1000)
                        9,10,11,12,13 -> Thread.sleep(200)
                        14 -> Thread.sleep(2500)
                        15 -> Thread.sleep(1000)
                    }
                }
            }
        }
    }

    fun ysf(){
        while (nowNum<aimNum){
            println("---------------------------------------------------")
            val sdf = SimpleDateFormat() // 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a") // a为am/pm的标记
            val date = Date() // 获取当前时间
            println("Time：" + sdf.format(date)) // 输出已经格式化的现在时间（24小时制）
            nowNum+=1
            println("Number: $nowNum")
            print("\n")
            for (i in locX.indices){
                clink(locX[i],locY[i])
                when(i){
                    4 -> Thread.sleep(3500)
                    6,7,8,9,10 -> Thread.sleep(500)
                    11 -> Thread.sleep(2000)
                    else -> Thread.sleep(1000)
                }
            }
        }
    }
}