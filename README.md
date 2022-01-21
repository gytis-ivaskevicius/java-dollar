# java-dollar
Java library that helps you to make more dollars :)

There are a bunch of common operations that are bothersome to write, goal of this library is to:
- Reduce verbosity
- Improve null safety
- Improve editor integration with help of [Contracts](https://www.jetbrains.com/help/idea/contract-annotations.html) and [nullable/notNull annotations](https://www.jetbrains.com/help/idea/nullable-and-notnull-annotations.html)

### Current list of methods: (Feel free to create issues with function requests :) )
```
defaultIfBlank
defaultIfEmpty
equals
filter
find
flatMapTo
getIfBlank
getIfEmpty
ifNotNull
ifNull
isBlank
isEmpty
listEqualsIgnoreOrder
map
mapNotNull
or
toMap
trim
```

### A couple of examples:
```java
class Examples {
    public List<String> dataMapping() {
        final var input = getInput();



        // java-dollar approach
        $.map(input, it -> it.getName());



        // stream approach
        input.stream()
            .map(it -> it.getName())
            .collect(Collectors.toList());



        // procedural approach
        final var result = new ArrayList<String>();
        for (var it : input) {
            result.add(it.getName());
        }
        return result;
    }

    public List<String> dataMappingAndFiltering() {
        final var input = getInput();



        // java-dollar approach
        $.mapNotNull(input, it -> it.getName());



        // stream approach
        input.stream()
                .map(it -> it.getName())
                .filter(it -> it != null)
                .collect(Collectors.toList());



        // procedural approach
        final var result = new ArrayList<String>();
        for (var it : input) {
            var value = it.getName();
            if(value != null) {
                result.add();
            }
        }
        return result;
    }
}
```
[A couple of tests](https://github.com/gytis-ivaskevicius/java-dollar/blob/master/src/test/java/io/gytis/dollar/TestBenchmark.java#L49-L94)

### Future work: (Expected to be completed in a couple of days)
- Publish to maven repository
- 100% Mutational test coverage
- Micro-optimizations + Improve safety
