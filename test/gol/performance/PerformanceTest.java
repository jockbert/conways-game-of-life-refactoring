package gol.performance;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import gol.execution.Executor;
import gol.execution.Result;
import gol.execution.SeparateProcessExecutor;
import gol.execution.TimedExecutor;

/**
 * Step 1 - Remove @Ignore annotation.
 * 
 * Step 2 - Before improving the production code, adjust baselineMillis to
 * actual baseline running time so performance factor ends up around 1.0.
 * 
 * Step 3 - Improve production code.
 * 
 * Step 4 - ?
 * 
 * Step 5 - Profit.
 */
@Ignore
public class PerformanceTest {

	private final static String LARGE_FILE = "examples/perfTestLarge";
	private final static String EXTREME_FILE = "examples/perfTestExtreme";

	private String title;
	private String arguments;
	private double baselineMillis;

	@Test
	public void testQuietLongSimulation() {
		ensureStartStateFile(150, 150, LARGE_FILE);

		title = "Quiet Long Simulation";
		arguments = " -s 1000 -q -w 0 -h 0 -l 1000 -f " + LARGE_FILE;

		baselineMillis = 21322;
	}

	@Test
	public void testManyStepsOutputForSparseGrowingSizeSimulation() {
		title = "Many Steps Output Performance";
		arguments = "-f examples/largeGlidersAndBlinkers -s 400";

		baselineMillis = 20032;
	}

	@Test
	public void testExtremeRead() {
		ensureStartStateFile(4000, 100000, EXTREME_FILE);

		title = "Extreme Read";
		arguments = "-s 0 -w 0 -h 0 -f " + EXTREME_FILE;

		baselineMillis = 5326;
	}

	@Test
	public void testExtremeReadWrite() {
		ensureStartStateFile(4000, 100000, EXTREME_FILE);

		title = "Extreme Read Write";
		arguments = "-s 0 -f " + EXTREME_FILE;

		baselineMillis = 9578;
	}

	@Test
	public void testGeneratingRandomStartState() {
		title = "Generating Random Start State";
		arguments = "-w 4000 -h 4000 -s 0";

		baselineMillis = 16791;
	}

	@After
	public void after() {
		println("");
		println(title);

		long actualMills = getExecutionMillis();
		String pf = performancefactor(baselineMillis, actualMills);

		println("%s ms (baseline %s ms)", actualMills, baselineMillis);
		println("Performance factor %s compared to baseline", pf);
	}

	private long getExecutionMillis() {
		Result result = new Result.IgnoreLinesResult(new Result.Empty());

		Executor runner = new TimedExecutor(new SeparateProcessExecutor());
		runner.executeProgram(arguments, result);

		return result.getExecutionTimeMillis();
	}

	private String performancefactor(double baseline, double actual) {
		NumberFormat formatter = new DecimalFormat("0.0");
		return formatter.format(baseline / actual);
	}

	private void ensureStartStateFile(int width, int height, String path) {
		File file = new File(path);
		if (file.exists())
			return;

		println("");
		println("Creating start state file " + path);

		try {
			file.createNewFile();
			PrintWriter out = new PrintWriter(file);
			Random random = new Random();
			for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x)
					out.print(random.nextBoolean() ? '#' : '-');
				out.println();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void println(String line, Object... objects) {
		System.out.printf(line, objects);
		System.out.println();
	}
}
