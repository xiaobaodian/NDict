package com.threecats.ndict.OriginalData

import com.threecats.ndict.Models.Food
import com.threecats.ndict.Models.FoodCategory

/**
 * 由 zhang 于 2018/1/31 创建
 */
object InitFoods {

    fun createFoods(foodCategorys: List<FoodCategory>){

        //谷类及其制品
        var category: FoodCategory? = foodCategorys.find { it.categoryID == 1 }
        category?.foods?.add(Food("稻米"))
        category?.foods?.add(Food("粳米"))
        category?.foods?.add(Food("籼米"))
        category?.foods?.add(Food("小麦"))
        category?.foods?.add(Food("糯米"))
        category?.foods?.add(Food("黑米"))
        category?.foods?.add(Food("米饭（蒸）"))
        category?.foods?.add(Food("小麦粉（糖白粉）"))
        category?.foods?.add(Food("小麦粉（标准粉）"))
        category?.foods?.add(Food("面条（切面）"))
        category?.foods?.add(Food("面条（挂面）"))
        category?.foods?.add(Food("馒头（富强粉）"))
        category?.foods?.add(Food("馒头（标准粉）"))
        category?.foods?.add(Food("油饼"))
        category?.foods?.add(Food("油条"))
        category?.foods?.add(Food("荞麦"))
        category?.foods?.add(Food("荞麦面"))
        category?.foods?.add(Food("小米"))
        category?.foods?.add(Food("玉米（鲜）"))
        category?.foods?.add(Food("玉米（白、鲜）"))
        category?.foods?.add(Food("玉米（黄、干）"))
        category?.foods?.add(Food("玉米面（黄）"))
        category?.foods?.add(Food("燕麦"))
        category?.foods?.add(Food("薏米"))

        //干豆类及其制品
        category = foodCategorys.find { it.categoryID == 2 }
        category?.foods?.add(Food("黄豆"))
        category?.foods?.add(Food("黄豆粉"))
        category?.foods?.add(Food("绿豆"))
        category?.foods?.add(Food("小豆"))
        category?.foods?.add(Food("豌豆"))
        category?.foods?.add(Food("芸豆"))
        category?.foods?.add(Food("豆浆（粉）"))
        category?.foods?.add(Food("腐竹"))
        category?.foods?.add(Food("粉丝（干）"))
        category?.foods?.add(Food("百叶（薄）千张"))
        category?.foods?.add(Food("豆腐（干）"))
        category?.foods?.add(Food("豆腐皮"))
        category?.foods?.add(Food("豆腐"))
        category?.foods?.add(Food("豆浆"))
        category?.foods?.add(Food("豆腐脑"))


        //鲜豆类及其制品
        category = foodCategorys.find { it.categoryID == 3 }
        category?.foods?.add(Food("毛豆（鲜）"))
        category?.foods?.add(Food("饭豇豆"))
        category?.foods?.add(Food("长豇豆"))
        category?.foods?.add(Food("豌豆"))
        category?.foods?.add(Food("蚕豆（鲜）"))
        category?.foods?.add(Food("扁豆"))
        category?.foods?.add(Food("百合"))
        category?.foods?.add(Food("黄豆芽"))
        category?.foods?.add(Food("绿豆芽"))
        category?.foods?.add(Food("菜豆角（芸扁豆角）"))
        category?.foods?.add(Food("豇豆（鲜豆荚）"))
        category?.foods?.add(Food("豌豆苗"))
        category?.foods?.add(Food("芸豆"))

        //根茎类
        category = foodCategorys.find { it.categoryID == 4 }
        category?.foods?.add(Food("地瓜（白薯）"))
        category?.foods?.add(Food("红薯（甘薯）"))
        category?.foods?.add(Food("马铃薯（土豆）"))
        category?.foods?.add(Food("芋头"))
        category?.foods?.add(Food("山药（鲜）"))
        category?.foods?.add(Food("藕"))
        category?.foods?.add(Food("竹笋"))
        category?.foods?.add(Food("大头菜"))
        category?.foods?.add(Food("萝卜"))
        category?.foods?.add(Food("青萝卜"))
        category?.foods?.add(Food("胡萝卜"))
        category?.foods?.add(Food("胡萝卜（黄）"))
        category?.foods?.add(Food("茭白（茭瓜）"))
        category?.foods?.add(Food("荸荠（马蹄）"))
        category?.foods?.add(Food("芦笋"))

        //蔬菜类
        category = foodCategorys.find { it.categoryID == 5 }
        category?.foods?.add(Food("油菜"))
        category?.foods?.add(Food("白菜（黄芽白）"))
        category?.foods?.add(Food("大白菜（青白口）"))
        category?.foods?.add(Food("小白菜"))
        category?.foods?.add(Food("雪里蕻（皱叶芥、芥菜）"))
        category?.foods?.add(Food("芥菜"))
        category?.foods?.add(Food("菠菜"))
        category?.foods?.add(Food("甘蓝（卷心菜）"))
        category?.foods?.add(Food("蕨菜"))
        category?.foods?.add(Food("茼蒿"))
        category?.foods?.add(Food("荠菜"))
        category?.foods?.add(Food("菜花"))
        category?.foods?.add(Food("西兰花"))
        category?.foods?.add(Food("芹菜（旱芹）"))
        category?.foods?.add(Food("金针菜"))
        category?.foods?.add(Food("大蒜（独头蒜）"))
        category?.foods?.add(Food("大蒜"))
        category?.foods?.add(Food("韭菜"))
        category?.foods?.add(Food("小葱"))
        category?.foods?.add(Food("大葱"))
        category?.foods?.add(Food("洋葱"))
        category?.foods?.add(Food("莴笋"))
        category?.foods?.add(Food("苋菜（绿）"))
        category?.foods?.add(Food("苋菜（红）"))
        category?.foods?.add(Food("生菜"))
        category?.foods?.add(Food("豆瓣菜"))
        category?.foods?.add(Food("芥蓝"))
        category?.foods?.add(Food("芫荽（香菜）"))
        category?.foods?.add(Food("蒜苗"))
        category?.foods?.add(Food("苜蓿（金花菜）"))
        category?.foods?.add(Food("木耳菜"))
        category?.foods?.add(Food("枸杞"))
        category?.foods?.add(Food("芹菜"))
        category?.foods?.add(Food("芥蓝"))
        category?.foods?.add(Food("榨菜"))

        //瓜茄类
        category = foodCategorys.find { it.categoryID == 6 }
        category?.foods?.add(Food("冬瓜"))
        category?.foods?.add(Food("黄瓜"))
        category?.foods?.add(Food("丝瓜"))
        category?.foods?.add(Food("苦瓜"))
        category?.foods?.add(Food("南瓜"))
        category?.foods?.add(Food("越瓜（菜瓜）"))
        category?.foods?.add(Food("西葫芦"))
        category?.foods?.add(Food("番茄"))
        category?.foods?.add(Food("茄子"))
        category?.foods?.add(Food("辣椒（鲜）"))
        category?.foods?.add(Food("柿子椒"))
        category?.foods?.add(Food("香椿"))
        category?.foods?.add(Food("仙人掌"))
        category?.foods?.add(Food("芦荟"))
        category?.foods?.add(Food("秋葵"))
        category?.foods?.add(Food("红薯（白皮）"))

        //水果类
        category = foodCategorys.find { it.categoryID == 7 }
        category?.foods?.add(Food("苹果"))
        category?.foods?.add(Food("富士苹果"))
        category?.foods?.add(Food("鸭梨"))
        category?.foods?.add(Food("梨子"))
        category?.foods?.add(Food("橙"))
        category?.foods?.add(Food("芦柑"))
        category?.foods?.add(Food("橘"))
        category?.foods?.add(Food("香蕉"))
        category?.foods?.add(Food("菠萝"))
        category?.foods?.add(Food("桃"))
        category?.foods?.add(Food("葡萄"))
        category?.foods?.add(Food("葡萄干"))
        category?.foods?.add(Food("柿子（盖柿）"))
        category?.foods?.add(Food("橘子"))
        category?.foods?.add(Food("柑"))
        category?.foods?.add(Food("金橘"))
        category?.foods?.add(Food("柚子"))
        category?.foods?.add(Food("杏"))
        category?.foods?.add(Food("李子"))
        category?.foods?.add(Food("杨桃"))
        category?.foods?.add(Food("草莓"))
        category?.foods?.add(Food("山楂"))
        category?.foods?.add(Food("柠檬"))
        category?.foods?.add(Food("芒果"))
        category?.foods?.add(Food("荔枝"))
        category?.foods?.add(Food("龙眼"))
        category?.foods?.add(Food("石榴"))
        category?.foods?.add(Food("木瓜"))
        category?.foods?.add(Food("枇杷"))
        category?.foods?.add(Food("橄榄"))
        category?.foods?.add(Food("椰子"))
        category?.foods?.add(Food("甜瓜"))
        category?.foods?.add(Food("西瓜"))
        category?.foods?.add(Food("哈密瓜"))
        category?.foods?.add(Food("枣（干）"))
        category?.foods?.add(Food("枣"))
        category?.foods?.add(Food("无花果"))
        category?.foods?.add(Food("甘蔗"))
        category?.foods?.add(Food("桑葚"))
        category?.foods?.add(Food("红毛丹"))
        category?.foods?.add(Food("樱桃"))
        category?.foods?.add(Food("猕猴桃"))
        category?.foods?.add(Food("西番莲（鸡蛋果）"))
        category?.foods?.add(Food("火龙果"))
        category?.foods?.add(Food("榴莲"))

        //坚果类
        category = foodCategorys.find { it.categoryID == 8 }
        category?.foods?.add(Food("花生"))
        category?.foods?.add(Food("花生仁（生）"))
        category?.foods?.add(Food("板栗（熟）"))
        category?.foods?.add(Food("核桃"))
        category?.foods?.add(Food("腰果"))
        category?.foods?.add(Food("开心果"))
        category?.foods?.add(Food("莲子"))
        category?.foods?.add(Food("芡实"))
        category?.foods?.add(Food("葵花籽"))
        category?.foods?.add(Food("西瓜子"))
        category?.foods?.add(Food("杏仁"))
        category?.foods?.add(Food("松子"))
        category?.foods?.add(Food("榛子"))
        category?.foods?.add(Food("栀子"))

        //畜牧产品
        category = foodCategorys.find { it.categoryID == 9 }
        category?.foods?.add(Food("猪肉（瘦）"))
        category?.foods?.add(Food("猪肉"))
        category?.foods?.add(Food("猪大排"))
        category?.foods?.add(Food("猪肋排"))
        category?.foods?.add(Food("猪肉（肥）"))
        category?.foods?.add(Food("猪肝"))
        category?.foods?.add(Food("猪肾（猪腰子）"))
        category?.foods?.add(Food("猪心"))
        category?.foods?.add(Food("猪肚子"))
        category?.foods?.add(Food("猪蹄"))
        category?.foods?.add(Food("猪舌"))
        category?.foods?.add(Food("火腿肠"))
        category?.foods?.add(Food("牛肉（瘦）"))
        category?.foods?.add(Food("牛肉"))
        category?.foods?.add(Food("牛肚"))
        category?.foods?.add(Food("牛肝"))
        category?.foods?.add(Food("牛蹄筋"))
        category?.foods?.add(Food("牛肾"))
        category?.foods?.add(Food("羊肉（瘦）"))
        category?.foods?.add(Food("羊肉"))
        category?.foods?.add(Food("羊肝"))
        category?.foods?.add(Food("羊肚"))
        category?.foods?.add(Food("兔肉"))
        category?.foods?.add(Food("马肉"))
        category?.foods?.add(Food("驴肉（瘦）"))

        //禽蛋类
        category = foodCategorys.find { it.categoryID == 10 }
        category?.foods?.add(Food("鸡"))
        category?.foods?.add(Food("乌鸡肉"))
        category?.foods?.add(Food("鸡肝"))
        category?.foods?.add(Food("鸭肉"))
        category?.foods?.add(Food("鹅"))
        category?.foods?.add(Food("鸽肉"))
        category?.foods?.add(Food("鹌鹑肉"))
        category?.foods?.add(Food("鸡蛋（白皮）"))
        category?.foods?.add(Food("鸡蛋（红皮）"))
        category?.foods?.add(Food("鸡蛋白"))
        category?.foods?.add(Food("鸡蛋黄"))
        category?.foods?.add(Food("鸭蛋"))
        category?.foods?.add(Food("鸭蛋（咸）"))
        category?.foods?.add(Food("松花蛋"))
        category?.foods?.add(Food("鹌鹑蛋"))
        category?.foods?.add(Food("鹅蛋"))
        category?.foods?.add(Food("鸽蛋"))

        //乳类及其制品
        category = foodCategorys.find { it.categoryID == 11 }
        category?.foods?.add(Food("牛乳"))
        category?.foods?.add(Food("羊乳"))
        category?.foods?.add(Food("人乳"))
        category?.foods?.add(Food("酸奶"))
        category?.foods?.add(Food("甜炼乳"))
        category?.foods?.add(Food("乳粉（全脂）"))
        category?.foods?.add(Food("奶油"))
        category?.foods?.add(Food("稀奶油"))

        //水产及其制品
        category = foodCategorys.find { it.categoryID == 12 }
        category?.foods?.add(Food("水产及其制品"))
        category?.foods?.add(Food("水产及其制品"))

        //菌类及其制品
        category = foodCategorys.find { it.categoryID == 13 }
        category?.foods?.add(Food("菌类及其制品"))
        category?.foods?.add(Food("菌类及其制品"))

        //调味制品
        category = foodCategorys.find { it.categoryID == 14 }
        category?.foods?.add(Food("调味制品"))
        category?.foods?.add(Food("调味制品"))

        //酿制产品
        category = foodCategorys.find { it.categoryID == 15 }
        category?.foods?.add(Food("酿制产品"))
        category?.foods?.add(Food("酿制产品"))

        //糖果辅食
        category = foodCategorys.find { it.categoryID == 16 }
        category?.foods?.add(Food("糖果辅食"))
        category?.foods?.add(Food("糖果辅食"))
    }
}