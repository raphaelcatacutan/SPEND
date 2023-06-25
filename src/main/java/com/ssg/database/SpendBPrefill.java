package com.ssg.database;

import com.ssg.MainActivity;

import java.io.File;
import java.sql.SQLException;
import java.time.Year;
import java.util.Objects;


public class SpendBPrefill {
    public static final String lorem200 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut orci nec quam auctor vestibulum. Duis vel massa vel sem pretium bibendum. Suspendisse potenti. Nunc euismod, quam id gravida consequat, turpis urna vestibulum lorem, ac pharetra veliteee";

    public static void generate() throws SQLException {
        Object[][] usersData = {
                {"Administrator", "A", "Administrator", "admin", "admin", 1},
                {"Raphael", "C", "Catacutan", "a", "a", 1},
                {"Raphael", "C", "Catacutan", "r", "r", 1},
                {"John Paul", "A", "Reyes", "jpreyes", "Johnpaul01", 0},
                {"Vince", "M", "De Jesus", "vincedejusus", "Vincedj098", 1}
        };
        Object[][] officerData = {
                {"Stephen", "A", "Bugna", lorem200, "President", "ICT", 1, 2021, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\B (1).jpg"},
                {"JC", "G", "Santos", lorem200, "Vice President", "GAS", 2, 2021, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\B (2).jpg"},
                {"Christine", "P", "Dela Cruz", lorem200, "Secretary", "HUMSS", 3, 2021, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\B (3).jpg"},
                {"Laurence", "J", "Antonio", lorem200, "Auditor", "HE", 1, 2021, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\B (4).jpg"},
                {"Gabriel", "C", "Frias", lorem200, "Project Manager", "STEM", 2, 2021, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\B (5).jpg"},
                {"Andrew", "N", "Quinto", lorem200, "Financial Officer", "SPORTS", 3, 2022, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\G (1).jpg"},
                {"Jasmine", "O", "Alonzo", lorem200, "Executive Assistant", "ICT", 1, 2022, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\G (2).jpg"},
                {"Wesley", "H", "Gomez", lorem200, "Executive Assistant", "GAS", 2, 2022, "G:\\RESOURCES\\GENERATED PROFILE PICTURE\\G (3).jpg"},
                {"Mark James", "L", "Austria", lorem200, "Finance Manager", "HUMSS", 3, 2022},
                {"Luis", "B", "Diaz", lorem200, "Analyst", "HE", 1, 2022},
                {"Samantha", "M", "Villanueva", lorem200, "Analyst", "STEM", 2, 2023},
                {"Jayson", "E", "Sultan", lorem200, "Representative", "SPORTS", 3, 2023},
                {"Noel", "P", "Flores", lorem200, "Administrative Assistant", "ICT", 1, 2023},
                {"Jenny", "A", "Natividad", lorem200, "Administrative Assistant", "GAS", 2, 2023},
                {"Justine", "C", "Molina", lorem200, "Graphic Designer", "HUMSS", 3, 2023}
        };
        Object[][] projectsData = {
                {"Feeding Program", lorem200, 1, "2023-04-13"},
                {"Recycling Program", lorem200, 2, "2023-01-10"},
                {"School Garden", lorem200, 3, "2023-03-20"},
                {"Collect and Donate School Supplies", lorem200, 1, "2023-04-24"},
                {"Organize School Club", lorem200, 2, "2023-02-03"},
                {"School Volunteer", lorem200, 3, "2023-03-15"},
                {"Leadership Training", lorem200, 1, "2023-01-30"},
                {"Anti-drug Campaign", lorem200, 2, "2023-04-28"},
                {"Zero Waste Management", lorem200, 3, "2023-05-15"},
                {"Smoke free school campaign", lorem200, 1, "2023-01-26"},
                {"Brigada Eskwela", lorem200, 2, "2023-08-12"},
                {"Reading and Tutoring Program", lorem200, 3, "2023-01-25"},
                {"Peer Counseling Program", lorem200, 1, "2023-06-23"},
                {"Career Guidance Program", lorem200, 2, "2023-07-03"},
                {"Music Fest", lorem200, 3, "2023-03-17"},
                {"Sports Fest", lorem200, 1, "2023-05-25"},
                {"Lost and Found Box", lorem200, 2, "2023-03-13"},
                {"Outreach Activity", lorem200, 3, "2023-05-05"},
                {"School Fund Campaign", lorem200, 1, "2023-02-16"},
                {"Improvement of School Campaign", lorem200, 2, "2023-07-12"},
                {"Safety Procedures Campaign", lorem200, 3, "2023-05-10"},
                {"Alcohol-free School Campaign", lorem200, 1, "2023-03-06"},
                {"Poster Making Contest", lorem200, 2, "2023-07-19"},
                {"Slogan Making Contest", lorem200, 3, "2023-07-20"},
                {"Free School Wifi Program", lorem200, 1, "2023-01-23"},
                {"School Toilet Cleaning Program", lorem200, 2, "2023-05-29"},
                {"School Zumba Program", lorem200, 3, "2023-07-14"},
                {"Plant Trees and Flowers Campaign", lorem200, 1, "2023-07-10"},
                {"Chair Repair Program", lorem200, 2, "2023-06-09"},
                {"Book Donation for Library", lorem200, 3, "2023-03-02"}
        };
        Object[][] expensesData = {
                {1, "Pineapple", 4500.22, 4.25, 5.3, 0},
                {1, "Coal", 4493.54, 2.73, 42.04, 1},
                {1, "Coffee", 4848.78, 9.23, 21.44, 1},
                {1, "Aluminum", 1110.07, 2.65, 94.37, 0},
                {1, "Leather", 1934.35, 9.59, 37.22, 0},
                {1, "Petroleum", 2553.41, 3.39, 82.65, 1},
                {1, "Asbestos", 2724.04, 9.81, 38.07, 1},
                {1, "Nickel", 4214.6, 9.96, 29.38, 1},
                {1, "Cottonseed", 1810.02, 4.24, 61.54, 1},
                {1, "Limestone", 4221.37, 8.67, 34.27, 0},
                {1, "Zinc", 1248.05, 4.47, 56.97, 0},
                {1, "Sugar", 2002.78, 1.55, 8.7, 1},
                {1, "Lead", 1085.83, 7.99, 75.99, 1},
                {1, "Chromium", 2911.63, 6.12, 34.28, 1},
                {1, "Titanium", 2750.35, 3.19, 20.88, 1},
                {1, "Jute", 4397.82, 1.19, 35.26, 0},
                {1, "Paper", 2111.11, 5.1, 54.65, 0},
                {1, "Magnesium", 2693.29, 1.99, 99.45, 1},
                {1, "Tungsten", 3199.49, 3.02, 36.38, 1},
                {2, "Glass", 2745.82, 2.29, 56.61, 0},
                {3, "Rice", 3321.29, 1.93, 58.47, 1},
                {4, "Gypsum", 1819.04, 3.22, 54.81, 1},
                {5, "Watermelon", 4745.06, 1.27, 3.04, 0},
                {6, "Clay", 3607.91, 6.54, 66.13, 0},
                {7, "Cinnamon", 2612.47, 3.11, 59.3, 1},
                {8, "Graphite", 1208.23, 7.36, 48.29, 1},
                {9, "Salt", 4322.14, 3.48, 85.39, 0},
                {10, "Amber", 4717.87, 7, 29.21, 1},
                {11, "Wood", 2532.06, 7.98, 80.46, 0},
                {12, "Iron", 3408.41, 9.28, 87.41, 0},
                {13, "Rubber", 2352.79, 3.91, 19.32, 0},
                {14, "Rye", 1032.32, 1.65, 52.27, 0},
                {15, "Emerald", 2890.51, 5.43, 23.49, 1},
                {16, "Gold", 2929.58, 1.47, 28.49, 1},
                {17, "Rosewood", 1817.94, 9.03, 98.27, 1},
                {18, "Hemp", 4599.14, 2.12, 7.87, 0},
                {19, "Marble", 2187.71, 6.5, 22.75, 0},
                {20, "Silver", 1147.77, 8.61, 33.57, 0},
                {21, "Platinum", 4758.87, 2.52, 76.53, 1},
                {22, "Sandstone", 2939.44, 9.14, 55.76, 0},
                {23, "Cobalt", 3012.16, 2.14, 33.21, 0},
                {24, "Sapphire", 4852.07, 1.35, 7, 1},
                {25, "Diamond", 2201.24, 8.34, 87.03, 1},
                {26, "Talc", 3961.23, 4.91, 81.29, 1},
                {27, "Beryllium", 4725.46, 5.95, 46.62, 0},
                {28, "Gravel", 1420.81, 4.55, 89.6, 0},
                {29, "Ceramics", 2452.03, 2.49, 82.7, 1},
                {30, "Jade", 2249.89, 9.75, 52.4, 0},
                {1, "Mica", 1269.13, 6, 98.7, 1},
                {2, "Steel", 3655.56, 3.68, 67.55, 0},
                {3, "Manganese", 1449.15, 8.18, 41.5, 1},
                {4, "Copper", 2811.11, 2.24, 74.26, 0},
                {5, "Wool", 2916.67, 9.51, 80.37, 1},
                {6, "Nickel-copper", 2207.51, 3.87, 88.81, 1},
                {7, "Feldspar", 3279.61, 4.44, 36.87, 1},
                {8, "Bronze", 4107.16, 4.69, 83.49, 1},
                {9, "Zinc oxide", 2254.44, 4.74, 25.47, 0},
                {10, "Silk", 3665.26, 6.76, 5.14, 1},
                {11, "Pig iron", 1466.33, 3.8, 81.39, 0},
                {12, "Olive oil", 1744.41, 5.82, 37.04, 0},
                {13, "Platinum group", 1004.97, 1.87, 34.82, 1},
                {14, "Yarn", 2273.27, 7.29, 59.53, 0},
                {15, "Graphene", 2398.66, 1.37, 66.15, 0},
                {16, "Uranium", 1390.35, 9.8, 4.78, 0},
                {17, "Quicksilver", 1363.08, 7.27, 4.52, 1},
                {18, "Sea salt", 2268.12, 5.81, 32.08, 0},
                {19, "Lemon juice", 1317.86, 4.36, 42.39, 1},
                {20, "Tin", 4471.38, 8.4, 33.97, 0},
                {21, "Oats", 2583.96, 5.77, 23.3, 1},
                {22, "Chromium ore", 2127.24, 5.64, 36.37, 1},
                {23, "Molybdenum", 2922.22, 3.38, 64.95, 1},
                {24, "Paint pigments", 3151.17, 7.28, 46.56, 1},
                {25, "Petroleum jelly", 3914.42, 3.02, 31.03, 0},
                {26, "Coal tar", 4145.67, 1.36, 47.77, 1},
                {27, "Antimony", 4572.47, 2.62, 23.07, 0},
                {28, "Palm oil", 4363.38, 7.76, 31.94, 0},
                {29, "Chalk", 3899.23, 6.95, 15.69, 0},
                {30, "Clay minerals", 2018.71, 3.73, 61.52, 1},
                {1, "Tea", 3678.05, 1.96, 83.26, 1},
                {2, "Uranium oxide", 2035.69, 3.98, 18.2, 1},
                {3, "Natural gas", 3123.99, 2.54, 17.08, 0},
                {4, "Bronze alloys", 2778.59, 5.46, 63.94, 1},
                {5, "Stone", 2541.7, 3.2, 11.18, 0},
                {6, "Turquoise", 4092.9, 2.72, 54.35, 0},
                {7, "Cane sugar", 3240.95, 5.04, 86.35, 0},
                {8, "Wheat", 4376.06, 2.5, 9.44, 0},
                {9, "Cornstarch", 2744.68, 2.35, 50.64, 0},
                {10, "Silver nitrate", 4520.38, 9.24, 53.47, 1},
                {11, "Ochre", 4703.3, 2.35, 45.81, 0},
                {12, "Obsidian", 4029.68, 6.04, 83.93, 1},
                {13, "Fluorspar", 1764.48, 5.28, 16.43, 1},
                {14, "Fertilizer", 3306.09, 6.86, 30.99, 1},
                {15, "Kaolin", 3974.37, 8.19, 10.92, 0},
                {16, "Magnesite", 3508.47, 1.36, 61.54, 1},
                {17, "Glycerol", 3336.42, 1.9, 50.38, 1},
                {18, "Diamond cutting tools", 4020.88, 1.34, 42.66, 0},
                {19, "Rice bran", 2234.1, 6.11, 93.05, 0},
                {20, "Olive wood", 2668.59, 8.05, 67.95, 1},
                {21, "Silver halide", 1520.73, 5.18, 96.55, 0},
                {22, "Vanilla", 2418.75, 6.05, 34.93, 0},
                {23, "Soybean oil", 4528.76, 1.14, 80.24, 0},
                {24, "Quartzite", 1300.17, 9.49, 32.52, 0},
                {25, "Refined sugar", 4667.72, 9.99, 85.44, 0},
                {26, "Sandpaper", 2771.45, 6.07, 33.4, 0},
                {27, "Indium", 1043.29, 1.57, 82.29, 1},
                {28, "Cattle hides", 1294.29, 9.75, 45.54, 1},
                {29, "Hardwood", 3885.99, 5.68, 73.7, 0},
                {30, "Asphalt", 4712.13, 1.24, 45.33, 0},
                {1, "Citrus oil", 2523.01, 3.18, 70.25, 1},
                {2, "Kaolinite", 4624.68, 2.23, 12.89, 0},
                {3, "Bauxite", 3547.24, 3.03, 93.91, 1},
                {4, "Peanuts", 4291, 6.42, 27.35, 0},
                {5, "Magnesium alloys", 3834.42, 4.49, 16.96, 1},
                {6, "Shellac", 2182.15, 6.51, 23.6, 0},
                {7, "Pine wood", 2391.43, 6.59, 21.82, 0},
                {8, "Rice husk", 4476.29, 4.73, 63.4, 1},
                {9, "Anthracite", 4406.76, 2.61, 41.21, 1},
                {10, "Ginkgo biloba", 3978.36, 7.13, 47.13, 0},
                {11, "Soy protein", 1951.35, 9.23, 86.42, 1},
                {12, "Silicon carbide", 4554.73, 2.55, 81.74, 0},
                {13, "Magnesium oxide", 3089.86, 3.22, 86.98, 0},
                {14, "Cow milk", 2172.45, 4.51, 34.9, 0},
                {15, "Borax", 3775.87, 4.79, 69.61, 0},
                {16, "Magnesium sulfate", 4785.28, 2.66, 46.45, 0},
                {17, "Flax", 2727.11, 6.99, 41.95, 0}, {18, "Barite", 4131.29, 2.29, 68.38, 1}, {19, "Bentonite", 2498.35, 4.21, 88.05, 0}, {20, "Stainless steel", 1613.73, 8.88, 55.91, 1}, {21, "Vinegar", 3593.15, 1.49, 40.7, 1}, {22, "Chromium compounds", 1959.87, 9.5, 4.61, 0}, {23, "Gypsum plaster", 1483.23, 3.38, 16.74, 0}, {24, "Cotton linter", 4751.18, 5.71, 47.36, 0}, {25, "Hemp oil", 4102.91, 3.75, 52.64, 0}, {26, "Wool felt", 3930.73, 1.94, 29.64, 0}, {27, "Graphite electrodes", 3740.02, 1.09, 35.21, 0}, {28, "Cottonseed oil", 2833.58, 3.03, 28.83, 1}, {29, "Tungsten carbide", 1316.1, 9.13, 99, 1}, {30, "Lignite", 1632.09, 9.2, 51.52, 1}, {1, "Furs", 1643.91, 7.93, 32.58, 1}, {2, "Lumber", 3311.91, 1.11, 33.53, 0}, {3, "Sulfuric acid", 3862.76, 5.95, 78.47, 1}, {4, "Titanium dioxide", 4146.3, 9.8, 6.72, 0}, {5, "Coconut oil", 2389.15, 3.11, 59.17, 0}, {6, "Boron", 4717.53, 8.05, 1.07, 1}, {7, "Plasticizers", 3511.45, 2.99, 84.92, 0}, {8, "Synthetic rubber", 3031.23, 2.91, 39.39, 0}, {9, "Carob", 3292.07, 9.09, 82.3, 1}, {10, "Methanol", 1381.41, 4.77, 4.87, 1}, {11, "Corn", 3414.22, 3.2, 8.37, 0}, {12, "Copper", 3599.19, 1.86, 84.96, 0}, {13, "Cotton", 4537.77, 4.64, 81.4, 1}, {14, "Fish", 3749.89, 1.23, 31.02, 0}, {15, "Gold", 2095.73, 3.74, 91.74, 0}, {16, "Grapes", 2236.06, 4.27, 99.67, 0}, {17, "Iron", 2672.92, 4.9, 2.35, 1}, {18, "Lumber", 1045.05, 4.01, 10.21, 0}, {19, "Marble", 3792.4, 8.97, 72.96, 1}, {20, "Nickel", 1132.8, 5.28, 1.45, 0}, {21, "Oil", 1905.99, 7.72, 58.66, 1}, {22, "Potatoes", 1771.82, 2.01, 38.68, 0}, {23, "Rubber", 1840.3, 4.41, 8.4, 0}, {24, "Salt", 2266.63, 7.15, 60.78, 0}, {25, "Silver", 2395.66, 7.56, 60.58, 1}, {26, "Soybeans", 3013.29, 5.15, 63.21, 1}, {27, "Sugar", 3919.28, 4, 50.65, 1}, {28, "Wheat", 1988.96, 5.37, 7.34, 0}, {29, "Zinc", 1346.42, 2.94, 21.88, 0}, {1, "Bamboo", 2922.53, 6.31, 62.38, 0}, {1, "Granite", 1141.32, 9.45, 80.42, 1}
        };
        Object[][] contributorsData = {
                {1, 1}, {1, 2}, {1, 3}, {1, 4}, {2, 5}, {2, 6}, {2, 7}, {2, 8}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {4, 13}, {4, 14}, {4, 15}, {4, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13}, {8, 14}, {8, 15}, {8, 1}, {8, 2}, {9, 3}, {9, 4}, {9, 5}, {9, 6}, {10, 7}, {10, 8}, {10, 9}, {10, 10}, {11, 11}, {11, 12}, {11, 13}, {11, 14}, {12, 15}, {12, 1}, {12, 2}, {12, 3}, {13, 4}, {13, 5}, {13, 6}, {13, 7}, {14, 8}, {14, 9}, {14, 10}, {14, 11}, {15, 12}, {15, 13}, {15, 14}, {15, 15}, {16, 1}, {16, 2}, {16, 3}, {16, 4}, {17, 5}, {17, 6}, {17, 7}, {17, 8}, {18, 9}, {18, 10}, {18, 11}, {18, 12}, {19, 13}, {19, 14}, {19, 15}, {19, 1}, {20, 2}, {20, 3}, {20, 4}, {20, 5}, {21, 6}, {21, 7}, {21, 8}, {21, 9}, {22, 10}, {22, 11}, {22, 12}, {22, 13}, {23, 14}, {23, 15}, {23, 1}, {23, 2}, {24, 3}, {24, 4}, {24, 5}, {24, 6}, {25, 7}, {25, 8}, {25, 9}, {25, 10}, {26, 11}, {26, 12}, {26, 13}, {26, 14}, {27, 15}, {27, 1}, {27, 2}, {27, 3}, {28, 4}, {28, 5}, {28, 6}, {28, 7}, {29, 8}, {29, 9}, {29, 10}, {29, 11}, {30, 12}, {30, 13}, {30, 14}, {30, 15}
        };
        Object[][] schoolData = {{
                Year.now().getValue() - 1,
                new File(Objects.requireNonNull(MainActivity.class.getResource("assets/icons/school-logo.png")).getFile()).getAbsolutePath().replace("%20", " "),
                new File(Objects.requireNonNull(MainActivity.class.getResource("assets/icons/school-logo.png")).getFile()).getAbsolutePath().replace("%20", " "),
                // FIXME Get SSG Logo
                "G:\\Downloads",
                1,
                "C:\\xampp",
                1,
                1,
                "Ms. Nermie Dela Paz",
                "Mr. Alord Somera",
                lorem200
        }};
        Object[][] fundsData = {{
                200.0,
                lorem200
        }};

        try {
            for (Object[] x : usersData) SpendBCreate.createUser(x, false);
            for (Object[] x : officerData) SpendBCreate.createOfficer(x, false);
            for (Object[] x : projectsData) SpendBCreate.createProject(x, false);
            for (Object[] x : expensesData) SpendBCreate.createExpenses(x, false);
            for (Object[] x : contributorsData) SpendBCreate.createContributors(x, false);
            for (Object[] x : schoolData) SpendBCreate.createSchoolData(x, false);
            for (Object[] x : fundsData) SpendBCreate.createFundsData(x, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}