package gol.world;

public interface WorldSource {

	WorldSourceResult generate();

	interface WorldSourceResult {
		World world();

		int width();

		int height();
	}

	static WorldSourceResult result(World world, int width, int height) {
		return new WorldSourceResult() {

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
