package util;

import com.example.rubank.Account;
import com.example.rubank.AccountDatabase;

/**
 * Sort class containing sorting methods for accounts and lists.
 * @author Eric Lin Anish Mande
 */

public class Sort {

    public static <E> void account(List<E> list, char key) {
        if (list == null || list.isEmpty()) {
            return;
        }

        quickSort(list, 0, list.size() - 1, key);
    }
    public static void account(AccountDatabase list, char key) {
        if (list == null || list.isEmpty()) {
            return;
        }
        quickSort(list, 0, list.size() - 1, key);
    }

    private static <E> void quickSort(List<E> list, int low, int high, char key) {
        if (low < high) {
            int pi = partition(list, low, high, key);
            quickSort(list, low, pi - 1, key);
            quickSort(list, pi + 1, high, key);
        }
    }
    private static <E> int partition(List<E> list, int low, int high, char key) {
        E pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(list.get(j), pivot, key) < 0) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    private static <E> int compare(E e1, E e2, char key) {
        if (e1 instanceof Account && e2 instanceof Account) {
            Account a1 = (Account) e1;
            Account a2 = (Account) e2;
            switch (key) {
                case 'B': // Sort by Branch
                    int branchComparison = a1.getNumber().getBranch().getCounty().compareTo(a2.getNumber().getBranch().getCounty());
                    if (branchComparison != 0)
                        return branchComparison;
                    int numberComparison = a1.getNumber().getBranch().compareTo(a2.getNumber().getBranch());
                    if (numberComparison != 0)
                        return numberComparison;
                    return a1.getNumber().toString().compareTo(a2.getNumber().toString());
                case 'H': // Sort by Profile (Account Holder)
                    int holderComparison = a1.getHolder().compareTo(a2.getHolder());
                    if (holderComparison != 0)
                        return holderComparison;
                    return a1.getNumber().toString().compareTo(a2.getNumber().toString());
                case 'T': // Sort by AccountType
                    int typeComparison = a1.getNumber().getType().compareTo(a2.getNumber().getType());
                    if (typeComparison != 0)
                        return typeComparison;
                    return a1.getNumber().toString().compareTo(a2.getNumber().toString());
                default:
                    throw new IllegalArgumentException("Invalid sorting key: " + key);
            }
        }
        throw new IllegalArgumentException("Sorting type error.");
    }

    private static <E> void swap(List<E> list, int i, int j) {
        E temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
