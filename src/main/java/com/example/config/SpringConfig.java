package com.example.config;


import com.example.Utility;
import com.example.menu.FirstMenu;
import com.example.menu.MainMenu;
import com.example.menu.Menu;
import com.example.menu.SecondMenu;
import java.util.List;
import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class SpringConfig {
  @Bean
  public Random random() {
<<<<<<< HEAD
<<<<<<< HEAD
    return new Random();
=======
    return new Random(42);
>>>>>>> 014d8fb (22_01_2023_01_03_PM_Task_2_3)
=======
    return new Random();
>>>>>>> 9e37042 (22_01_2023_01_22_PM_Task_2_3)
  }
  @Bean
  public MainMenu firstMenu(@FirstMenu List<Menu> items) {
    return createMenu(items);
  }

  @Bean
  public MainMenu secondMenu(@SecondMenu List<Menu> items) {
    return createMenu(items);
  }

  @Bean
  public Utility utility() {
    return new Utility();
  }

  private MainMenu createMenu(List<Menu> items) {
    MainMenu menu = new MainMenu(utility());
    items.forEach(menu::addMenuItem);
    return menu;
  }
}
