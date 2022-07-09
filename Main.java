package census;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        Collection<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)])
            );
        }

        // Найти количество несовершеннолетних (т.е. людей младше 18 лет).
        Stream<Person> minorsStream = persons.stream();
        long countOfMinors = minorsStream
                .filter(person -> person.getAge() < 18)
                .count();

        System.out.println(countOfMinors);
        System.out.println();

        // Получить список фамилий призывников (т.е. мужчин от 18 и до 27 лет).
        Stream<Person> conscriptsStream = persons.stream();

        Predicate<Person> isConscript = person ->
                person.getAge() >= 18 &&
                        person.getAge() < 27 &&
                        person.getSex().equals(Sex.MAN);

        List<String> listOfConscripts = conscriptsStream
                .filter(isConscript)
                .map(Person::getFamily)
                .toList();

        for (String s : listOfConscripts) {
            System.out.println(s);
        }
        System.out.println();

        // Получить отсортированный по фамилии список потенциально работоспособных людей с высшим образованием
        // в выборке (т.е. людей с высшим образованием от 18 до 60 лет для женщин и до 65 лет для мужчин).
        Stream<Person> personStream = persons.stream();

        Predicate<Person> isPotentialEmployee = person ->
                person.getAge() >= 18 &&
                        person.getEducation().equals(Education.HIGHER) &&
                        (
                                (
                                        person.getSex().equals(Sex.WOMAN) &&
                                                person.getAge() < 60
                                ) || (
                                        person.getSex().equals(Sex.MAN) &&
                                                person.getAge() < 65
                                )
                        );

        Comparator<Person> comparator = Comparator.comparing(Person::getFamily);

        List<Person> potentialEmployees = personStream
                .filter(isPotentialEmployee)
                .sorted(comparator)
                .collect(Collectors.toList());

        for (Person p : potentialEmployees) {
            System.out.println(p);
        }
    }
}
