package com.ryan.gameshop.util;

import java.util.Random;

public class OrderRefer {
   public static Long generateRefer(int orderId) {
      Random random = new Random(orderId);
      int num = random.nextInt();
      return 15200000000L + num;
   }

}
