package gol;

import static gol.OutputPrimitives.blinkerHorizontal;
import static gol.OutputPrimitives.blinkerVertical;
import static gol.OutputPrimitives.block;
import static gol.OutputPrimitives.start;
import static gol.OutputPrimitives.step;

import org.junit.Test;

public class LoopDetectionTest extends AbstractTest {

	@Test
	public void detectOneStepLoopFail() {
		givenProgramArgs("-f examples/block -s 2 -l 0");

		output(block);
		output(start);

		output(block);
		output(step(1));

		output(block);
		output(step(2));
	}

	@Test
	public void detectOneStepLoopSuccess() {
		givenProgramArgs("-f examples/block -s 2 -l 1");

		output(block);
		output(start);

		output(block);
		output("step 1 - loop of length 1 detected");
		output();
	}

	@Test
	public void detectTwoStepLoopFail() {
		givenProgramArgs(" -s 3 -f examples/blinker -l 1 ");

		output(blinkerHorizontal);
		output(start);

		output(blinkerVertical);
		output(step(1));

		output(blinkerHorizontal);
		output(step(2));

		output(blinkerVertical);
		output(step(3));
	}

	@Test
	public void detectTwoStepLoopSuccess() {
		givenProgramArgs(" -s 3 -f examples/blinker -l 10 ");

		output(blinkerHorizontal);
		output(start);

		output(blinkerVertical);
		output(step(1));

		output(blinkerHorizontal);
		output("step 2 - loop of length 2 detected");
		output();
	}

	@Test
	public void detectFifteenStepLoopSuccess() {
		givenProgramArgs(" -s 100 -l 15 -f examples/pentadecathlon  ");

		outputHasStepCount(15);
	}

	@Test
	public void detectFifteenStepLoopFailure() {
		givenProgramArgs(" -s 100 -l 14 -f examples/pentadecathlon  ");

		outputHasStepCount(100);
	}

	@Test
	public void gliderIsNeverInALoop() {
		givenProgramArgs("-f examples/gliderRight -s 5 -w 5 -h 5 -l 10");

		output("-#---");
		output("--#--");
		output("###--");
		output("-----");
		output("-----");
		output(start);

		output("-----");
		output("#-#--");
		output("-##--");
		output("-#---");
		output("-----");
		output(step(1));

		output("-----");
		output("--#--");
		output("#-#--");
		output("-##--");
		output("-----");
		output(step(2));

		output("-----");
		output("-#---");
		output("--##-");
		output("-##--");
		output("-----");
		output(step(3));

		output("-----");
		output("--#--");
		output("---#-");
		output("-###-");
		output("-----");
		output(step(4));

		output("-----");
		output("-----");
		output("-#-#-");
		output("--##-");
		output("--#--");
		output(step(5));

	}

	@Test
	public void gliderOutsideOfViewportIsNeverInALoop() {
		// Also tests that alive cells left of view port is
		// handled right.
		givenProgramArgs("-f examples/gliderLeft -s 11 -l 100");

		output("###-----");
		output("#----###");
		output("-#------");
		output(start);

		output("##----#-");
		output("#-#---#-");
		output("------#-");
		output(step(1));

		output("#-#-----");
		output("#----###");
		output("--------");
		output(step(2));

		output("#-----#-");
		output("-#----#-");
		output("------#-");
		output(step(3));

		output("--------");
		output("#----###");
		output("--------");
		output(step(4));

		output("-#----#-");
		output("------#-");
		output("------#-");
		output(step(5));

		output("--------");
		output("-----###");
		output("--------");
		output(step(6));

		output("#-----#-");
		output("------#-");
		output("------#-");
		output(step(7));

		output("--------");
		output("-----###");
		output("--------");
		output(step(8));

		output("------#-");
		output("------#-");
		output("------#-");
		output(step(9));

		output("--------");
		output("-----###");
		output("--------");
		output(step(10));

		output("------#-");
		output("------#-");
		output("------#-");
		output(step(11));
	}

}
