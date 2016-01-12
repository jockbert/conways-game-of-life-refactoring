package gol;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class ArgumentParser {

	private Map<String, Runnable> flagMap = new HashMap<>();
	private Map<String, Consumer<String>> argMap = new HashMap<>();
	private Consumer<String> onError = s -> {
	};

	void apply(String[] args) {
		Iterator<String> it = Arrays.asList(args).iterator();

		while (it.hasNext()) {
			String arg = it.next();

			if (flagMap.containsKey(arg))
				flagMap.get(arg).run();
			else if (argMap.containsKey(arg))
				argMap.get(arg).accept(it.next());
			else
				onError.accept("Unknown argument " + arg);
		}
	}

	void onError(Consumer<String> onError) {
		this.onError = onError;
	}

	void flag(String flag, Runnable fn) {
		flagMap.put(flag, fn);
	}

	void intArg(String flag, Consumer<Integer> fn) {
		argMap.put(flag, toStrCons(fn));
	}

	void strArg(String flag, Consumer<String> fn) {
		argMap.put(flag, fn);
	}

	private Consumer<String> toStrCons(Consumer<Integer> intConsumer) {
		return str -> intConsumer.accept(parseInt(str));
	}

	private int parseInt(String s) {
		int n = Integer.parseInt(s);
		if (n < 0)
			onError.accept("Invalid argument value " + n);
		return n;
	}
}
