package benchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

//  Benchmark                      Mode  Cnt  Score   Error  Units
//  LongStreamBenchmark.longValue  avgt   40  2.055 ± 0.032  ms/op
//  LongStreamBenchmark.valueOf    avgt   40  7.372 ± 0.542  ms/op


@SuppressWarnings("ALL")
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"})
public class LongStreamBenchmark {
    private static final long N = 1_000_000L;
    private List<Long> nums;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public LongStreamBenchmark() {
        logger.info("Creating list of " + N + " longs");
        nums = LongStream.rangeClosed(1, N)
                        .boxed()
                        .collect(Collectors.toList());
    }

    @Benchmark
    public long valueOf() {
        return nums.stream()
                .mapToLong(Long::valueOf)
                .sum();
    }

    @Benchmark
    public long longValue() {
        return nums.stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    @TearDown(Level.Invocation)
    public void tearDown() {
        System.gc();
    }
}