package pu.porna.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 * N.B. Het Cars-voorbeeld komt van https://github.com/eugenp/tutorials/tree/master/spring-freemarker/src/main/java/com/baeldung/freemarker
 */
@Controller
public class CarsController
{
@SuppressWarnings( "unused" )
private static final Logger LOG = LoggerFactory.getLogger(CarsController.class);
private static List<Car> carList = new ArrayList<>();

@RequestMapping(value = "/", method = RequestMethod.GET)
public String home(Locale locale, Model model) {
    return "redirect:/cars";
}

static {
    carList.add(new Car("Honda", "Civic"));
    carList.add(new Car("Toyota", "Camry"));
    carList.add(new Car("Nissan", "Altima"));
}

@RequestMapping(value = "/cars", method = RequestMethod.GET)
public String init(@ModelAttribute("model") ModelMap model) {
    model.addAttribute("carList", carList);
    return "cars";
}

@RequestMapping(value = "/add", method = RequestMethod.POST)
public String addCar(@ModelAttribute("car") Car car) {
    if (null != car && null != car.getMake() && null != car.getModel() && !car.getMake().isEmpty() && !car.getModel().isEmpty()) {
        carList.add(car);
    }
    return "redirect:/cars";
}


}
