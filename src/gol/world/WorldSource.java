package gol.world;

public interface WorldSource {
	
	interface WorldSourceResult {
		 World world();
		 int width();
		 int height();		
	}

	WorldSourceResult generate();
}
