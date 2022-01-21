package io.gytis.dollar;

import org.junit.jupiter.api.Test;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@Threads(8)
@Measurement(iterations = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-server", "-Xbatch", "-XX:+AlwaysPreTouch", "-XX:CICompilerCount=1", "-XX:TieredStopAtLevel=1", "-Xms2G", "-Xmx2G"})
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1)
public class TestBenchmark {
    private static final int DOUBLE_DIGIT_FILE_NAME_LENGTH = "dummy-file-00".length();

    private static final Map<Integer, List<File>> TEST_CASES = Map.of(
            100, generateTestData(100),
            1000, generateTestData(1000),
            10000, generateTestData(10000)
    );

    private static List<File> generateTestData(int endExclusive) {
        return IntStream.range(0, endExclusive).boxed()
                .map(it -> new File("dummy-file-" + it))
                .collect(Collectors.toUnmodifiableList());
    }

    @Param({"100", "1000", "10000"})
    public int iterations;

    @Test
    @Benchmark
    public List<String> dollarMapNotNull() {
        final var input = TEST_CASES.get(iterations);

        return $.mapNotNull(input, it -> {
            return DOUBLE_DIGIT_FILE_NAME_LENGTH == it.getName().length()
                    ? null
                    : it.getName();
        });
    }

    @Test
    @Benchmark
    public List<File> dollarFilter() {
        final var input = TEST_CASES.get(iterations);
        return $.filter(input, it -> DOUBLE_DIGIT_FILE_NAME_LENGTH == it.getName().length());
    }

    @Test
    @Benchmark
    public Map<String, File> dollarToMapK() {
        final var input = TEST_CASES.get(iterations);
        return $.toMap(input, File::getName);
    }

    @Test
    @Benchmark
    public Map<String, Boolean> dollarToMapKV() {
        final var input = TEST_CASES.get(iterations);
        return $.toMap(input, File::getName, File::exists);
    }

    @Test
    @Benchmark
    public File dollarFind() {
        final var input = TEST_CASES.get(iterations);
        return $.find(input, it -> DOUBLE_DIGIT_FILE_NAME_LENGTH == it.getName().length());
    }

    @Test
    @Benchmark
    public List<String> dollarMap() {
        final var input = TEST_CASES.get(iterations);
        return $.map(input, File::getName);
    }

    @Test
    @Benchmark
    public List<String> streamMap() {
        return TEST_CASES.get(iterations).stream()
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Test
    @Benchmark
    public List<String> proceduralMap() {
        final var result = new ArrayList<String>();
        for (var it : TEST_CASES.get(iterations)) {
            result.add(it.getName());
        }
        return result;
    }

    @Test
    public void benchmark() throws Exception {
        String[] argv = {};
        org.openjdk.jmh.Main.main(argv);
    }

}
