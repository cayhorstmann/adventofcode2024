To run these programs, put your personalized input into `inputs/input1z`, `inputs/input2z`, and so on. I am not including them in the repo because Advent of Code asks that these files not be made public. The test inputs (`inputs/input1a`, `inputs/inputs2a`, and so on), are provided. Sometimes there are separate inputs for the two parts, such as `inputs/input3b`. Use suffixes c, d, and so on, for any additional tests, and edit `main()` to parse and run them.

Use Java 23 and --enable-preview. No need to compile.

```
java --enable-preview Day1.java
java --enable-preview Day2.java
...
```
    
As you can see, all solutions have a common structure:

* The file name is `Day`*n*`.java`
* Instance variables of the implicit class
* A `parse` method that reads the input and initializes the instance variables. Be sure to initialize them all!
* Methods `part1()` and `part2()` that provide the result for Part 1 and Part 2 of the puzzle

I realize that I could squeeze out a bit more commonality with a common superclass, but I wanted to keep the magic to a minimum. Instead, I just copy/paste this template every day:

```
void parse(Path path) throws IOException {
}

Object part1() {
    return null;
}

Object part2() {
    return null;
}

Path path(String suffix) { return Path.of("inputs/input" + Integer.parseInt(getClass().getName().replaceAll("\\D", "")) + suffix); }

void main() throws IOException {
    long start = System.nanoTime();
    parse(path("a"));
    IO.println(part1());
    IO.println(part2());
    parse(path("z"));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}

```

Then I fill `inputs/input`*n*`a` with the sample input, and `inputs/input`*n*`z` with the personalized input.
