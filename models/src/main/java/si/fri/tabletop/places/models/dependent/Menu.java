package si.fri.tabletop.places.models.dependent;

import java.util.List;

public class Menu {

    private String id;
    private String placeId;
    private List<Drink> menuDrinks;
    private List<Food> menuFood;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<Drink> getMenuDrinks() {
        return menuDrinks;
    }

    public void setMenuDrinks(List<Drink> menuDrinks) {
        this.menuDrinks = menuDrinks;
    }

    public List<Food> getMenuFood() {
        return menuFood;
    }

    public void setMenuFood(List<Food> menuFood) {
        this.menuFood = menuFood;
    }
}
