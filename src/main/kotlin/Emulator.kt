
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class Emulator {

    var aimNum:Int = 100
    var nowNum:Int = 0
    private val numX = arrayOf(534,188,539,896,188,539,896,188,539,896)
    private val numY = arrayOf(2089,1688,1688,1688,1826,1826,1826,1969,1969,1969)
    private val ysfX = arrayOf(663, 430, 310, 160, 955, 536, 206, 576, 902, 920, 879, 547, 1014)
    private val ysfY = arrayOf(332, 2089, 448, 419, 206, 2074, 1659, 1831, 1961, 1834, 1688, 1689, 118)
    private val locAlipayX = arrayOf(145, 995, 167, 502, 144, 502, 156, 942, 774, 922,922,525,525,599,599,975)
    private val locAlipayY = arrayOf(300, 132, 331, 2081, 2088, 2081, 1619, 2003, 2079, 1606,1606,1735,1735,1927,1927,133)
    private val pictureX = arrayOf(155, 411, 701, 942, 134,424,710)
    private val pictureY = arrayOf(357, 327, 350, 317, 580,594,586)
    private val password = "133997"
    var ipAddress = "101"
    private val adbHome = "adb" //"/home/mark/data/sdk/platform-tools/adb"
    private val realDevice = "beff28c7"
    private val ipDevice = "192.168.0.${ipAddress}:5555"
    private val device = realDevice
    private var nowActivity:String? = ""
    private var errorNum = 0
    private var scanErrorNum = 0
    private var payErrorNum = 0

    private fun clink(x:Int,y:Int){
        val cmd = arrayOf(adbHome, "-s",device,"shell", "input", "tap", x.toString(), y.toString())
        runCmd(cmd)
    }

    private fun runCmd(cmd:Array<String>){
        val p = Runtime.getRuntime().exec(cmd)
        p.waitFor()
        p.destroy()
    }

    private fun getTopActivity(){
        val cmd = arrayOf(adbHome, "-s",device,"shell", "dumpsys","activity","top","|","grep","ACTIVITY","|","grep","unionpay","|","grep","-v","'^$'","|","awk","-F","'/'","'{print $2}'","|", "awk", "'{print $1}'")
        val p = Runtime.getRuntime().exec(cmd)
        val br = BufferedReader(InputStreamReader(p.inputStream))
        while (br.readLine().also { nowActivity = it } != null){
            //println("line: $nowActivity")
            break
        }
        p.waitFor()
        p.destroy()
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
        for (i in 6..11){
            ysfX[i] = numX[password[i-6]-'0']
            ysfY[i] = numY[password[i-6]-'0']
        }
        while (nowNum<aimNum){
            println("---------------------------------------------------")
            val sdf = SimpleDateFormat() // 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a") // a为am/pm的标记
            val date = Date() // 获取当前时间
            println("Time：" + sdf.format(date)) // 输出已经格式化的现在时间（24小时制）
            nowNum+=1
            println("Number: $nowNum")
            print("\n")
            for (i in ysfX.indices){
                clink(ysfX[i],ysfY[i])
                when(i){
                    4 -> Thread.sleep(3000)
                    6,7,8,9,10 -> Thread.sleep(400)
                    11 -> Thread.sleep(1800)
                    else -> Thread.sleep(1000)
                }
            }
            /**操作错误判断*/
            getTopActivity()
            if (nowActivity!! == ".activity.UPActivityMain"){ //如果正常运行就进入下一个循环
                continue
            } else if (nowActivity == ".activity.react.UPActivityReactNative"){ // 如果卡在支付页面
                errorNum++
                clink(70,136)
                Thread.sleep(1100)
                getTopActivity()
                if (nowActivity == ".activity.react.UPActivityReactNative") { // 测试点击左上角后如果还没退出就点击右上角
                    payErrorNum++
                    println("Error is pay error")
                    while (nowActivity == ".activity.react.UPActivityReactNative"){ // 一直点返回直到退出
                        clink(997,139)
                        Thread.sleep(1100)
                    }
                }else{
                    scanErrorNum++
                    nowNum--
                    println("Error is scan error")
                }
                println("Total Error num: $errorNum   Pay error: $payErrorNum    Scan error: $scanErrorNum")
                println()
            }else{
                return
            }
        }
    }
}

//adb shell dumpsys activity top |grep ACTIVITY |grep unionpay |grep -v '^$' |awk -F '/' '{print $2}'  |awk '{print $1}'