package gol.world;

public interface WorldResult {

		World world();

		int width();

		int height();

	static WorldResult result(World world, int width, int height) {
		return new WorldResult() {

			@Override
			public World world() {
				return world;
			}

			@Override
			public int width() {
				return width;
			}

			@Override
			public int height() {
				return height;
			}
		};
	}
}
