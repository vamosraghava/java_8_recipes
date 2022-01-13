package manning;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

// From Java 8 and 9 in Action (now called Modern Java in Action)

// New M1 Max results (Jan 13, 2020):
// Benchmark                                        Mode  Cnt   Score   Error  Units
// ParallelStreamBenchmark.iterativeSum             avgt   10   3.244 ± 0.013  ms/op
// ParallelStreamBenchmark.sequentialStreamSum      avgt   10  58.416 ± 0.139  ms/op
// ParallelStreamBenchmark.parallelStreamSum        avgt   10  45.632 ± 0.690  ms/op
// ParallelStreamBenchmark.sequentialLongStreamSum  avgt   10   6.350 ± 0.028  ms/op
// ParallelStreamBenchmark.parallelLongStreamSum    avgt   10   0.484 ± 0.016  ms/op

@SuppressWarnings("ALL")
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"})
public class ParallelStreamBenchmark {
    private static final long N = 10_000_000L;

    @Benchmark
    public long iterativeSum() {
        long result = 0;
        for (long i = 1L; i <= N; i++) {
            result += i;
        }
        return result;
    }

    @Benchmark  // Slowest possible stream
    public long sequentialStreamSum() {
        return Stream.iterate(1L, i -> i + 1)  // Stream<Long>
                .limit(N)
                .reduce(0L, Long::sum);
    }

    @Benchmark
    public long parallelStreamSum() {
        return Stream.iterate(1L, i -> i + 1)
                .limit(N)
                .parallel()
                .reduce(0L, Long::sum);
    }

    @Benchmark // Fastest possible stream
    public long sequentialLongStreamSum() {
        return LongStream.rangeClosed(1, N).sum();
    }

    @Benchmark
    public long parallelLongStreamSum() {
        return LongStream.rangeClosed(1, N)
                .parallel()
                .sum();
    }

    @TearDown(Level.Invocation)
    public void tearDown() {
        System.gc();
    }
}