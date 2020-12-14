package onlineShop.models.products.computers;

import onlineShop.models.products.BaseProduct;
import onlineShop.models.products.Product;
import onlineShop.models.products.components.Component;
import onlineShop.models.products.peripherals.Peripheral;

import java.util.ArrayList;
import java.util.List;


import static onlineShop.common.constants.ExceptionMessages.*;
import static onlineShop.common.constants.OutputMessages.COMPUTER_COMPONENTS_TO_STRING;
import static onlineShop.common.constants.OutputMessages.COMPUTER_PERIPHERALS_TO_STRING;

public abstract class BaseComputer extends BaseProduct implements Computer {
    private List<Component> components;
    private List<Peripheral> peripherals;

    protected BaseComputer(int id, String manufacturer, String model, double price, double overallPerformance) {
        super(id, manufacturer, model, price, overallPerformance);
        this.components = new ArrayList<>();
        this.peripherals = new ArrayList<>();
    }


    @Override
    public double getOverallPerformance() {
        if (this.components.isEmpty()) {
            return this.overallPerformance;
        }
        double average = this.components.stream().mapToDouble(Component::getOverallPerformance).average().orElse(0);
        return this.overallPerformance + average;
    }

    @Override
    public double getPrice() {
        double sumComponents = this.components.stream().mapToDouble(Component::getPrice).sum();
        double sumPeripherals = this.peripherals.stream().mapToDouble(Product::getPrice).sum();

        return this.price + sumComponents + sumPeripherals;
    }

    @Override
    public List<Component> getComponents() {
        return this.components;
    }

    @Override
    public List<Peripheral> getPeripherals() {
        return this.peripherals;
    }

    @Override
    public void addComponent(Component component) {
        for (Component c : components) {
            if (c.getClass().getSimpleName().equals(component.getClass().getSimpleName())) {
                throw new IllegalArgumentException(String.format(EXISTING_COMPONENT, component.getClass().getSimpleName(), this.getClass().getSimpleName(), getId()));

            }
        }
        components.add(component);

    }

    @Override
    public Component removeComponent(String componentType) {
        int componentIndex = -1;
        if (components.isEmpty()) {
            throw new IllegalArgumentException(String.format(NOT_EXISTING_COMPONENT, componentType, this.getClass().getSimpleName(), this.getId()));
        }
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getClass().getSimpleName().equals(componentType)) {
                componentIndex = i;
            }
            if (componentIndex == -1) {
                throw new IllegalArgumentException(String.format(NOT_EXISTING_COMPONENT, componentType, this.getClass().getSimpleName(), this.getId()));
            }
        }

        return components.remove(componentIndex);
    }

    @Override
    public void addPeripheral(Peripheral peripheral) {
        for (Peripheral p : peripherals) {
            if (p.getClass().getSimpleName().equals(peripheral.getClass().getSimpleName())) {
                throw new IllegalArgumentException(String.format(EXISTING_PERIPHERAL, peripheral.getClass().getSimpleName(), this.getClass().getSimpleName(), getId()));

            }
        }
        peripherals.add(peripheral);
    }

    @Override
    public Peripheral removePeripheral(String peripheralType) {
        int peripheralIndex = -1;
        if (peripherals.isEmpty()) {
            throw new IllegalArgumentException(String.format(NOT_EXISTING_PERIPHERAL, peripheralType, this.getClass().getSimpleName(), this.getId()));
        }
        for (int i = 0; i < peripherals.size(); i++) {
            if (peripherals.get(i).getClass().getSimpleName().equals(peripheralType)) {
                peripheralIndex = i;
            }
            if (peripheralIndex == -1) {
                throw new IllegalArgumentException(String.format(NOT_EXISTING_PERIPHERAL, peripheralType, this.getClass().getSimpleName(), this.getId()));
            }
        }

        return peripherals.remove(peripheralIndex);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(System.lineSeparator());
        sb.append(String.format(COMPUTER_COMPONENTS_TO_STRING, getComponents().size())).append(System.lineSeparator());
        for (Component component : components) {
            sb.append("  ").append(component).append(System.lineSeparator());
        }


        double average = this.peripherals.stream().mapToDouble(Product::getOverallPerformance).average().orElse(0);
        sb.append(String.format(COMPUTER_PERIPHERALS_TO_STRING, getPeripherals().size(), average));
        sb.append(System.lineSeparator());

        for (Peripheral peripheral : peripherals) {
            sb.append("  ").append(peripheral).append(System.lineSeparator());

        }


        return sb.toString().trim();
    }
}
