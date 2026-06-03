package com.polar.lithos;

import java.util.HashMap;

import static com.badlogic.gdx.math.MathUtils.floor;

public final class ChunkManager {

    private final HashMap<ChunkCoord, Chunk> loadedChunks = new HashMap<>();

    public Chunk getChunk(int chunkX, int chunkY) {
        ChunkCoord chunkCoord = new ChunkCoord(chunkX, chunkY);

        if (loadedChunks.containsKey(chunkCoord)) {
            try {
                return loadedChunks.get(chunkCoord);        // Fetches existing chunk

            } catch (NullPointerException e) {
                System.err.printf("Failed to fetch existing chunk at [%d, %d]: %s%n", chunkX, chunkY, e.getMessage());
                return null;
            }
        } else {
            try {
                Chunk chunk = new Chunk(chunkCoord);
                loadedChunks.put(chunkCoord, chunk); // Generates new chunk
                return chunk;

            } catch (NullPointerException e) {
                System.err.printf("Failed to generate new chunk at [%d, %d]: %s%n", chunkX, chunkY, e.getMessage());
                loadedChunks.remove(chunkCoord);
                System.err.println("Removed failed chunk from loadedChunks map");
            }
        }
        return null; // No chunk get sent due to Exceptions
    }

    // we need a way to get a chunk and if it doesn't exist we create it, remember to check loaded chunks

    public int getBlockAtWorldPos (int worldX, int worldY) {
        // Convert block position to Chunk Coordinate
            // e.g. x = 349, y = 60 is Chunk coordinate: 21, 3
        int chunkX = floor((float) worldX / 16);
        int chunkY = floor((float) worldY / 16);

        Chunk chunk = getChunk(chunkX, chunkY);

        // Useless??? since getChunk already has it implemented there (.getBlock)
        ChunkCoord chunkCoord = new ChunkCoord(chunkX, chunkY);

        // Convert World coordinate to in-chunk coordinate (from 0 -> 15)
        int localX = (worldX % 16 + 16) % 16;
        int localY = (worldY % 16 + 16) % 16;

        return chunk.getBlock(localX, localY);
    }

    public void setBlockAtWorldPos (int worldX, int worldY, int blockId) {
        // Convert block position to Chunk Coordinate
        // e.g. x = 349, y = 60 is Chunk coordinate: 21, 3
        int chunkX = floor((float) worldX / 16);
        int chunkY = floor((float) worldY / 16);

        Chunk chunk = getChunk(chunkX, chunkY);

        // Convert World coordinate to in-chunk coordinate (from 0 -> 15)
        int localX = (worldX % 16 + 16) % 16;
        int localY = (worldY % 16 + 16) % 16;

        chunk.setBlock(localX, localY, blockId);
    }
}
