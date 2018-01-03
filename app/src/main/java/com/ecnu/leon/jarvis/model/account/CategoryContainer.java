package com.ecnu.leon.jarvis.model.account;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

public class CategoryContainer {
    Context context;
    private static final String FILENAME = "Category.dat";
    public ArrayList<Category> categories;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public CategoryContainer(Context context) {
        this.context = context;
        this.categories = new ArrayList<Category>();
    }

    public boolean addRootCategory(Category category) {
        // 筛掉名称相等的
        for (int i = 0; i < categories.size(); i++) {
            if (category.getTitleString().equalsIgnoreCase(categories.get(i).getTitleString())) {
                Toast.makeText(context, "有重复项", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        this.categories.add(category);
        return true;
    }

    public boolean removeRootCategory(Category category) {
        if (this.categories.size() == 0 || this.categories == null)
            return false;

        for (int i = 0; i < categories.size(); i++) {
            if (category.getTitleString().equalsIgnoreCase(categories.get(i).getTitleString())) {
                categories.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
        out.writeObject(this.categories);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILENAME));
        this.categories = (ArrayList<Category>) in.readObject();
        in.close();
        return false;
    }


    public boolean addDefaultCategory() {
        String mainString[] =
                {"餐饮", "交通", "购物", "娱乐", "医教", "居家", "投资", "人情", "生意"};
        for (int i = 0; i < mainString.length; i++) {
            Category mainCategory = new Category(mainString[i]);
            switch (mainCategory.getTitleString().trim()) {
                case "餐饮": {
                    String string[] =
                            {"早餐", "午餐", "晚餐", "饮料水果", "零食", "买卖原料", "夜宵", "油盐酱醋", "餐饮其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        switch (category.getTitleString()) {
                            case "早餐":
                                category.setPinned(true);
                                break;
                            case "午餐":
                                category.setPinned(true);
                                break;
                            case "晚餐":
                                category.setPinned(true);
                                break;
                            case "饮料水果":
                                category.setPinned(true);
                                break;

                            default:
                                break;
                        }
                        mainCategory.categories.add(category);
                    }
                }
                break;
                case "交通": {
                    String string[] =
                            {"打车", "公交", "加油", "停车费", "地铁", "火车", "长途汽车", "过路过桥", "维修保养", " 飞机", "车款车贷", "罚款赔偿", "车险", "自行车", "船舶", "驾照费用", "交通其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                break;
                case "购物": {
                    String string[] =
                            {"服饰鞋包", "居家百货", "烟酒", "化妆护肤", "电子数码", "宝宝用品", "家居家纺", "报刊书籍", "电器", "珠宝首饰", "文具玩具", "保健用品", "摄影文印", "购物其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                break;
                case "娱乐": {
                    String string[] =
                            {"旅游度假", "网游电玩", "电影", "洗浴足浴", "运动健身", "卡拉OK", "茶酒咖啡", "歌舞演出", "电视", "花鸟宠物", "麻将棋牌", "聚会玩乐", "娱乐其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                case "医教": {
                    String string[] =
                            {"医疗药品", "挂号门诊", "养生保健", "住院费", "养老院", "学杂教材", "培训考试", "家教补习", "学费", "幼儿教育", "出国留学", "助学贷款", "医教其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                break;
                case "居家": {
                    String string[] =
                            {"手机电话", "房款房贷", "水电燃气", "美发美容", "住宿房租", "材料建材", "电脑宽带", "快递邮政", "物业", "家政服务", "生活费", "婚庆摄影", "漏记款", "保险费", "消费贷款", "税费手续费", "生活其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                break;
                case "投资": {
                    String string[] =
                            {"股票", "基金", "理财产品", "余额宝", "银行存款", "保险", "P2P", "证券期货", "出资", "贵金属", "投资贷款", "外汇", "收藏品", "利息支出", "投资其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                break;
                case "人情": {
                    String string[] =
                            {"礼金红包", "物品", "请客", "代付款", "孝敬", "给予", "慈善捐款", "人情其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                break;
                case "生意": {
                    String string[] =
                            {"进货采购", "人工支出", "材料辅料", "工程付款", "交通运输", "运营费", "会务费", "办公费用", "营销广告", "店面租金", "注册登记", "生意其他"};
                    for (int j = 0; j < string.length; j++) {
                        Category category = new Category(string[j]);
                        mainCategory.categories.add(category);
                    }
                }
                break;

                default:
                    break;
            }

            categories.add(mainCategory);
        }

        return true;
    }

    public boolean generatePinnedCategory() {
        // 筛掉不要的
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getTitleString().equalsIgnoreCase("常用")) {
                categories.remove(i);
            }
        }

        Category pinnedCategory = new Category("常用");
        pinnedCategory.categories = getPinnedCategories();

        categories.add(0, pinnedCategory);
        return true;
    }

    public ArrayList<Category> getPinnedCategories() {
        ArrayList<Category> pinnedCategories = new ArrayList<Category>();
        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < categories.get(i).categories.size(); j++) {
                Category currentCategory = categories.get(i).categories.get(j);
                if (currentCategory.isPinned() == true) {
                    pinnedCategories.add(currentCategory);
                }
            }
        }

        return pinnedCategories;
    }

}
