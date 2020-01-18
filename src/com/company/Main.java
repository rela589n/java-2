package com.company;

import com.company.database.OrderInfo;
import com.company.database.generic.Database;
import com.company.database.generic.EntityNotFoundException;

import java.io.IOException;
import java.util.*;

public class Main {
    private Scanner consoleScanner;
    private Database db = null;

    public Main() {
        this.consoleScanner = new Scanner(System.in);
        this.consoleScanner.useDelimiter("\\n");
    }

    private int menu() throws OperationNotFoundException {
        System.out.println("1 (Load), 2 (Save), 3 (Add el), 4 (Show all), 5 (Show one), 6 (Update), 7 (Delete), 8(Search by date), 99 (exit)");
        if (!consoleScanner.hasNextInt()) {
            throw new OperationNotFoundException("Invalid operation supplied.");
        }

        int ret = consoleScanner.nextInt();

        return ret;
    }

    private String getFileName() {
        System.out.print("Enter file name: ");
        return consoleScanner.next();
    }

    private OrderInfo getOrderInfo() {
        System.out.println("Enter product name: ");
        String name = consoleScanner.next();

        System.out.println("Enter producer name: ");
        String producer = consoleScanner.next();

        System.out.println("Enter measure unit: ");
        String measureUnit = consoleScanner.next();

        Calendar calendar = getDate();
        System.out.println("Enter count: ");
        int count = getConsoleIntOrMinus1();

        System.out.println("Enter price per one: ");
        float priceOne = getConsoleFloatOrMinus1();

        return new OrderInfo(name, producer, measureUnit, calendar, count, priceOne);
    }

    private String getTrimString() {
        return consoleScanner.next().trim();
    }

    private int getConsoleIntOrMinus1() {
        String raw = getTrimString();
        if (raw.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(raw);
    }

    private float getConsoleFloatOrMinus1() {
        String tmp = getTrimString();
        if (tmp.isEmpty()) {
            return -1f;
        }
        return Float.parseFloat(tmp);
    }

    private Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Enter year: ");
        calendar.set(Calendar.YEAR, getConsoleIntOrMinus1());

        System.out.println("Enter month: ");
        calendar.set(Calendar.MONTH, getConsoleIntOrMinus1());

        System.out.println("Enter day: ");
        calendar.set(Calendar.DAY_OF_MONTH, getConsoleIntOrMinus1());

        System.out.println("Enter hour: ");
        calendar.set(Calendar.HOUR_OF_DAY, getConsoleIntOrMinus1());

        System.out.println("Enter minute: ");
        calendar.set(Calendar.MINUTE, getConsoleIntOrMinus1());

        System.out.println("Enter seconds: ");
        calendar.set(Calendar.SECOND, getConsoleIntOrMinus1());

        return calendar;
    }

    private int readId() throws InvalidPropertiesFormatException {
        System.out.println("Enter id: ");
        if (!consoleScanner.hasNextInt()) {
            throw new InvalidPropertiesFormatException("Invalid id.");
        }
        return getConsoleIntOrMinus1();
    }

    private boolean isDateSame(Calendar c1, Calendar c2) {
        return (
                c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                        c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                        c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) &&
                        c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY) &&
                        c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE) &&
                        c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND)
        );
    }

    private void checkDatabaseForNull() {
        if (this.db == null) {
            throw new NullPointerException("Database is not loaded yet.");
        }
    }

    private void mergeOrders(OrderInfo into, OrderInfo source) {
        if (!source.getName().trim().isEmpty()) {
            into.setName(source.getName().trim());
        }

        if (!source.getProducer().trim().isEmpty()) {
            into.setProducer(source.getProducer().trim());
        }
        Calendar sourceDate = source.getAcquireDate();

        Calendar intoDate = into.getAcquireDate();
        if (sourceDate.get(Calendar.YEAR) != -1) {
            intoDate.set(Calendar.YEAR, sourceDate.get(Calendar.YEAR));
        }

        if (sourceDate.get(Calendar.MONTH) != -1) {
            intoDate.set(Calendar.MONTH, sourceDate.get(Calendar.MONTH));
        }

        if (sourceDate.get(Calendar.DAY_OF_MONTH) != -1) {
            intoDate.set(Calendar.DAY_OF_MONTH, sourceDate.get(Calendar.DAY_OF_MONTH));
        }

        if (sourceDate.get(Calendar.HOUR_OF_DAY) != -1) {
            intoDate.set(Calendar.HOUR_OF_DAY, sourceDate.get(Calendar.HOUR_OF_DAY));
        }

        if (sourceDate.get(Calendar.MINUTE) != -1) {
            intoDate.set(Calendar.MINUTE, sourceDate.get(Calendar.MINUTE));
        }

        if (sourceDate.get(Calendar.SECOND) != -1) {
            intoDate.set(Calendar.SECOND, sourceDate.get(Calendar.SECOND));
        }

        if (!source.getMeasureUnit().trim().isEmpty()) {
            into.setMeasureUnit(source.getMeasureUnit().trim());
        }

        if (source.getCount() != -1) {
            into.setCount(source.getCount());
        }

        if (source.getPriceForOne() != -1) {
            into.setPriceForOne(source.getPriceForOne());
        }
    }

    public void run() throws ClassNotFoundException, EntityNotFoundException {
        try {
            int op = menu();

            switch (op) {
                case 1:
                    this.db = new Database(getFileName());
                    break;

                case 2:
                    checkDatabaseForNull();
                    this.db.save();
                    System.out.println("Successfully saved");
                    break;

                case 3:
                    checkDatabaseForNull();
                    this.db.create(getOrderInfo());
                    System.out.println("Successfully inserted. Id: " + db.lastInsertId());
                    break;

                case 4:
                    checkDatabaseForNull();
                    if (this.db.isEmpty()) {
                        throw new EntityNotFoundException("Database is empty");
                    }

                    this.db.readAll().forEach((key, el) -> System.out.println(key + ": " + el));
                    break;

                case 5:
                    checkDatabaseForNull();
                    System.out.println(this.db.read(readId()));
                    break;

                case 6:
                    checkDatabaseForNull();
                    System.out.println("Enter order id to update");
                    int id = readId();
                    OrderInfo order = (OrderInfo) this.db.read(id);

                    System.out.println("Enter order information (press enter to skip): ");
                    OrderInfo info = getOrderInfo();

                    mergeOrders(order, info);
                    this.db.update(id, order);
                    break;

                case 7:
                    checkDatabaseForNull();
                    System.out.println("Enter order id to delete");
                    int idDel = readId();
                    this.db.delete(idDel);
                    System.out.println("Successfully deleted.");

                    break;

                case 8:
                    checkDatabaseForNull();
                    System.out.println("Enter start date: ");
                    Calendar st = getDate();

                    System.out.println("Enter end date: ");
                    Calendar end = getDate();

                    this.db.readAll().forEach((key, _el) -> {
                        OrderInfo el = (OrderInfo) _el;
                        Calendar orderDate = el.getAcquireDate();

                        if ((orderDate.after(st) && orderDate.before(end)) || (isDateSame(orderDate, st) && isDateSame(orderDate, end))) {
                            System.out.println(key + ": " + el);
                        }
                    });
                    break;
                case 99:
                    return;
                default:
                    throw new OperationNotFoundException("Operation index must be between 1 and 8");
            }

        } catch (Exception e) {

//            System.out.println(1234);
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        run();
    }

    public static void main(String[] args) {
        Main program = new Main();
        try {
            program.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
