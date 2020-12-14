package onlineShop.core;

import onlineShop.core.interfaces.Controller;
import onlineShop.models.products.Product;
import onlineShop.models.products.components.*;
import onlineShop.models.products.computers.Computer;
import onlineShop.models.products.computers.DesktopComputer;
import onlineShop.models.products.computers.Laptop;
import onlineShop.models.products.peripherals.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import static onlineShop.common.constants.ExceptionMessages.*;
import static onlineShop.common.constants.OutputMessages.*;

public class ControllerImpl implements Controller {
    private List<Computer> computers;
    private List<Peripheral> peripherals;
    private List<Component> components;

    public ControllerImpl() {
        this.computers = new ArrayList<>();
        this.peripherals = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    @Override
    public String addComputer(String computerType, int id, String manufacturer, String model, double price) {
        Computer computer;
        isExist(id);
        if ("DesktopComputer".equals(computerType)) {
            computer = new DesktopComputer(id, manufacturer, model, price);
        } else if ("Laptop".equals(computerType)) {
            computer = new Laptop(id, manufacturer, model, price);
        } else {
            throw new IllegalArgumentException(INVALID_COMPUTER_TYPE);
        }
        computers.add(computer);
        return String.format(ADDED_COMPUTER, id);
    }

    @Override
    public String addPeripheral(int computerId, int id, String peripheralType, String manufacturer, String model, double price, double overallPerformance, String connectionType) {
        isNoExist(computerId);
        if (this.peripherals.stream().anyMatch(p -> p.getId() == id)) {
            throw new IllegalArgumentException(EXISTING_PERIPHERAL_ID);
        }
        Peripheral peripheral;
        switch (peripheralType) {
            case "Headset":
                peripheral = new Headset(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Keyboard":
                peripheral = new Keyboard(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Monitor":
                peripheral = new Monitor(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Mouse":
                peripheral = new Mouse(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            default:
                throw new IllegalArgumentException(INVALID_PERIPHERAL_TYPE);
        }
        int index = -1;
        for (int i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId() == computerId) {
                index = i;
            }
        }

        peripherals.add(peripheral);
        computers.get(index).addPeripheral(peripheral);


        return String.format(ADDED_PERIPHERAL, peripheralType, id, computerId);
    }


    @Override
    public String removePeripheral(String peripheralType, int computerId) {
        isNoExist(computerId);
        int index = -1;
        for (int i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId() == computerId) {
                index = i;
            }
        }
        computers.get(index).removePeripheral(peripheralType);

        int peripheralIndex = -1;
        for (int i = 0; i < peripherals.size(); i++) {
            if (peripherals.get(i).getClass().getSimpleName().equals(peripheralType)) {
                peripheralIndex = i;
            }
        }

        if (peripheralIndex == -1) {
            throw new IllegalArgumentException();
        }

        return String.format(REMOVED_PERIPHERAL, peripheralType, peripherals.remove(peripheralIndex).getId());
    }

    @Override
    public String addComponent(int computerId, int id, String componentType, String manufacturer, String model, double price, double overallPerformance, int generation) {
        isNoExist(computerId);

        if (this.components.stream().anyMatch(c -> c.getId() == id)) {
            throw new IllegalArgumentException(EXISTING_COMPONENT_ID);
        }
        Component component;
        switch (componentType) {
            case "CentralProcessingUnit":
                component = new CentralProcessingUnit(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "Motherboard":
                component = new Motherboard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "PowerSupply":
                component = new PowerSupply(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "RandomAccessMemory":
                component = new RandomAccessMemory(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "SolidStateDrive":
                component = new SolidStateDrive(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "VideoCard":
                component = new VideoCard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            default:
                throw new IllegalArgumentException(INVALID_COMPONENT_TYPE);
        }

        int index = -1;
        for (int i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId() == computerId) {
                index = i;
            }
        }
        computers.get(index).addComponent(component);
        components.add(component);


        return String.format(ADDED_COMPONENT, componentType, id, computerId);
    }


    @Override
    public String removeComponent(String componentType, int computerId) {
        isNoExist(computerId);
        int index = -1;
        for (int i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId() == computerId) {
                index = i;
            }
        }
        int componentId = 0;
        int componentIndex = -1;
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getClass().getSimpleName().equals(componentType)) {
                componentIndex = i;
                componentId = components.get(componentIndex).getId();
            }
        }
        if (componentIndex == -1) {
            throw new IllegalArgumentException();
        }
        computers.get(index).getComponents().remove(componentIndex);

        components.remove(componentIndex);

        return String.format(REMOVED_COMPONENT, componentType, componentId);
    }

    @Override
    public String buyComputer(int id) {
        isNoExist(id);
        int index = -1;
        for (int i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId() == id) {
                index = i;
            }
        }
        return computers.remove(index).toString();
    }

    @Override
    public String BuyBestComputer(double budget) {
        Computer computer = this.computers.stream().filter(c -> c.getPrice() <= budget)
                .sorted(Comparator.comparingDouble(Product::getOverallPerformance).reversed()).findFirst().orElse(null);

        if (computer == null) {
            throw new IllegalArgumentException(String.format(CAN_NOT_BUY_COMPUTER, budget));
        }

        return computer.toString();
    }

    @Override
    public String getComputerData(int id) {
        isNoExist(id);
        int index = -1;
        for (int i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId() == id) {
                index = i;
            }
        }
        return computers.get(index).toString();
    }

    private void isNoExist(int id) {
        if (this.computers.stream().noneMatch(c -> c.getId() == id)) {
            throw new IllegalArgumentException(NOT_EXISTING_COMPUTER_ID);
        }
    }

    private void isExist(int id) {
        if (this.computers.stream().anyMatch(c -> c.getId() == id)) {
            throw new IllegalArgumentException(EXISTING_COMPUTER_ID);
        }
    }

}
