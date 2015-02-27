package com.codebreeze.incubator.nulls;

import com.google.common.base.Optional;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.google.common.base.Objects.firstNonNull;
import static java.util.Arrays.copyOf;
import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;
import static org.apache.commons.lang3.ArrayUtils.getLength;
import static org.apache.commons.lang3.ArrayUtils.toArray;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * * Problems:
 * ** Null is a conceptual hazard: what is it? ambiguous at best
 * ** Null is an implementation hazard: NPE
 * ** Null is a maintenance hazard: readability and clarity of code
 * ** Null is implicit in interfaces making them vague
 * <p/>
 * * Solutions:
 * ** Do Not Use Nulls, Assume They do not exist (+remove them at entrance and exit points)
 * ** Build the system such that it does NOT handle nulls in the service/businesslogic layer. Only in transformations
 * ** Use NullObject Pattern, with FirstNonNull methods.
 * ** Use Defaulted Maps/Collections (e.g. from apache commons collections utils
 * ** Use String/Collection/Array/Object utils from apache commons lang3 to deal with potentially null objects
 * ** Use Optional/Option/Maybe pattern on interfaces
 * ** Replace Nulls at the system periphery
 * ** Use groovy for transformations
 * ** create behaviours in maps
 */
public class NullBestPractices {

    private static Set<Person> findAllChildrenWithNullChecks(Set<Person> people) {
        Set<Person> children = new HashSet<Person>();
        if (people != null) {
            for (final Person person : people) {
                children.add(person);
                if (person != null && person.getChildren() != null) {
                    children.addAll(person.getChildren());
                }
            }
        }
        return children;
    }

    private static Set<Person> findAllChildrenWithoutNullChecks(Set<Person> people) {
        Set<Person> children = new HashSet<Person>();
        for (final Person person : people) {
            children.add(person);
            children.addAll(person.getChildren());
        }
        return children;
    }

    private static Person fromNameWithoutDefaulted(final String name) {
        Person a = new Person("a", 1, NullAddress.INSTANCE, Collections.<Person>emptySet());
        Person b = new Person("b", 2, NullAddress.INSTANCE, Collections.<Person>emptySet());
        NAME_PERSON_INDEX.put(a.getName(), a);
        NAME_PERSON_INDEX.put(b.getName(), b);
        //either check it is present, then act
        //or ignore it is not present, and let someone else act
        if (NAME_PERSON_INDEX.get(name) == null) {
            return NAME_PERSON_INDEX.get(name);
        } else {
            return NullPerson.INSTANCE;
        }
        //or
        //return NAME_PERSON_INDEX.get(name);
    }

    private static Person fromNameWithDefaulted(final String name) {
        Person a = new Person("a", 1, NullAddress.INSTANCE, Collections.<Person>emptySet());
        Person b = new Person("b", 2, NullAddress.INSTANCE, Collections.<Person>emptySet());
        NAME_PERSON_INDEX.put(a.getName(), a);
        NAME_PERSON_INDEX.put(b.getName(), b);
        return NAME_PERSON_INDEX.get(name);
    }

    private static Double milesToWheelsRatioNoExplicit(final Vehicle2 v2) {
        return v2.getMiles() / v2.getWheelCount();
    }

    private static Double milesToWheelsRatioExplicitly(final Vehicle1 v1) {
        return v1.getMiles().or(averageMiles()) / v1.getWheelCount();
    }

    private static Double averageMiles() {
        return 50000.00;
    }

    //you can even write the whole thing in some other language
    private static String getPostCodeGroovy(Person person) {
        return String.class.cast(evaluator(person, "person", "return person?.address?.postCode"));
    }

    private static String getPostCodeNotGroovy(Person person) {
        if (person != null && person.getAddress() != null) {
            return person.getAddress().getPostCode();
        } else {
            return null;
        }
    }

    private static Object evaluator(final Object target, final String targetName, String expression) {
        final Binding binding = new Binding();
        binding.setVariable(targetName, target);
        final GroovyShell shell = new GroovyShell(binding);

        Object value = shell.evaluate(expression);
        return String.class.cast(value);
    }

    private static void runAgainstSomeConditions(String a, String b, boolean c, Integer d) {
        if (a.equalsIgnoreCase(b) && c) {
            if (d == 2) {
                // do something
            } else {
                //do something else
            }
        } else {
            //do something even else
        }
    }

    //utils
    private static final Map<Boolean, Map<Boolean, Behaviour>> NESTED_BEHAVIOUR = defaultedMap(
            new HashMap<Boolean, Map<Boolean, Behaviour>>() {{
                put(true, defaultedMap(new HashMap<Boolean, Behaviour>() {{
                }}, new Factory<Behaviour>() {
                    @Override
                    public Behaviour create() {
                        return new Behaviour() {
                            @Override
                            public void act() {
                                //do something
                            }
                        };
                    }
                }));
            }},
            new Factory<Map<Boolean, Behaviour>>() {
                @Override
                public Map<Boolean, Behaviour> create() {
                    return defaultedMap(new HashMap<Boolean, Behaviour>(){{
                    }}, new Factory<Behaviour>() {
                        @Override
                        public Behaviour create() {
                            return new Behaviour() {
                                @Override
                                public void act() {
                                    //do something even else
                                }
                            };
                        }
                    });
                }
            });

    private static interface Behaviour {
        void act();
    }

    private static interface KeyB {
        void act();
    }

    public static final Factory<Person> NULL_PERSON_FACTORY = new Factory<Person>() {
        @Override
        public Person create() {
            return NullPerson.INSTANCE;
        }
    };

    private static final Map<String, Person> NAME_PERSON_INDEX = defaultedMap(
            new HashMap<String, Person>(),
            NULL_PERSON_FACTORY
    );

    private static interface Vehicle1 {
        Optional<Double> getMiles(); //explicitly expressing that there could be potentially no value here

        Integer getWheelCount(); //must (or null implicitly which is bad)
    }

    private static interface Vehicle2 {
        Double getMiles(); //explicitly expressing that there could be potentially no value here

        Integer getWheelCount(); //must (or null implicitly which is bad)
    }

    private static class Car1 implements Vehicle1 {

        @Override
        public Optional<Double> getMiles() {
            return Optional.absent();
        }

        @Override
        public Integer getWheelCount() {
            return 4;
        }
    }

    private static class Car2 implements Vehicle2 {

        @Override
        public Double getMiles() {
            return null;
        }

        @Override
        public Integer getWheelCount() {
            return 4;
        }
    }


    // notice null value objects for strings, numbers, objects, collections, arrays
    private static class Person {
        private final String name;
        private final int age;
        private final Address address;
        private final Set<Person> children = new TreeSet<Person>();

        private Person(String name, Integer age, Address address, Set<Person> children) {
            this.name = defaultIfBlank(name, "Not Available");
            this.age = denull(age);
            this.address = denull(address);
            this.children.addAll(denull(children));
        }

        private Person(String name, int age, Address address, Set<Person> children, boolean nothing) {
            this.name = name;
            this.age = age;
            this.address = address;
            if (children != null) {
                children.addAll(children);
            }
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Address getAddress() {
            return address;
        }

        public Set<Person> getChildren() {
            return children;
        }

        private int denull(Integer age) {
            return defaultIfNull(age, 0);
        }

        private Address denull(Address address) {
            return address == null ? NullAddress.INSTANCE : address;
        }

        private Set<Person> denull(Set<Person> children) {
            return firstNonNull(children, Collections.<Person>emptySet());
        }
    }

    private static class NullPerson extends Person {
        public static final NullPerson INSTANCE = new NullPerson();

        private NullPerson() {
            super("Not Available", -1, NullAddress.INSTANCE, Collections.<Person>emptySet());
        }
    }


    private static class Address {
        private static final String[] NO_ADDRESS_LINES = new String[0];
        private final String[] addressLines;
        private final String postCode;

        public Address(String[] addressLines, String postCode) {
            this.addressLines = copyOf(denull(addressLines), getLength(addressLines));
            this.postCode = postCode;
        }

        private String[] denull(String[] addressLines) {
            return firstNonNull(addressLines, NO_ADDRESS_LINES);
        }

        public String[] getAddressLines() {
            return copyOf(addressLines, addressLines.length);
        }

        public String getPostCode() {
            return postCode;
        }
    }

    private static class NullAddress extends Address {
        public static final NullAddress INSTANCE = new NullAddress();

        private NullAddress() {
            super(toArray("Not Available"), "Not Available");
        }
    }
}
