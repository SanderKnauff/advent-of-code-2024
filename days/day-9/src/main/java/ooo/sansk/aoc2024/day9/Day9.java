package ooo.sansk.aoc2024.day9;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Day9 {
    long runPartOne(String input) {
        final var disk = Disk.fromDenseFormat(input);

        disk.compress();

        return disk.calculateChecksum();
    }

    long runPartTwo(String input) {
        final var disk = Disk.fromDenseFormat(input);

        disk.defragment();

        return disk.calculateChecksum();
    }


    static class Disk {
        private final List<DiskBlock> blocks;
        private final int usedBlocks;

        private Disk(List<DiskBlock> blocks) {
            this.blocks = blocks;
            this.usedBlocks = (int) this.blocks.stream().filter(DiskBlock.File.class::isInstance).count();
        }

        long calculateChecksum() {
            long sum = 0;
            for (int i = 0; i < blocks.size(); i++) {
                final var block = blocks.get(i);
                if (!(block instanceof DiskBlock.File file)) {
                    continue;
                }

                sum += i * file.id();
            }

            return sum;
        }

        void compress() {
            int index = blocks.size() - 1;
            while (isCompressed()) {
                final var source = blocks.get(index);
                blocks.set(index, DiskBlock.empty());
                if (!(source instanceof DiskBlock.File)) {
                    index--;
                    continue;
                }

                for (int i = 0; i < blocks.size(); i++) {
                    final var target = blocks.get(i);
                    if (!(target instanceof DiskBlock.Empty)) {
                        continue;
                    }

                    blocks.set(i, source);
                    break;
                }

                index--;
            }
        }

        boolean isCompressed() {
            for (int i = usedBlocks; i < blocks.size(); i++) {
                final var block = blocks.get(i);
                if (block instanceof DiskBlock.File) {
                    return true;
                }
            }

            return false;
        }

        void defragment() {
            Set<Long> moved = new HashSet<>();
            long id = blocks.get(usedBlocks - 1).id();
            int size = 0;
            for (int i = blocks.size() - 1; i >= 0; i--) {
                final var block = blocks.get(i);

                if (id != block.id()) {
                    if (id != -1 && size > 0 && !moved.contains(id)) {
                        moveFileToFirstEmptySpace(id, i + 1, size);
                        moved.add(id);
                    }

                    if (block.id() == -1) {
                        size = 0;
                    } else {
                        size = 1;
                    }
                } else {
                    size++;
                }

                id = block.id();
            }
        }

        void moveFileToFirstEmptySpace(long id, int offsetOfFileToMove, int size) {
            int space = 0;
            boolean moved = false;
            for (int i = 0; i < offsetOfFileToMove; i++) {
                final var block = blocks.get(i);
                if (block instanceof DiskBlock.File) {
                    space = 0;
                    continue;
                }

                space++;

                if (space >= size) {
                    int indexOfEmptySpaceForFile = i - (size - 1);
                    for (int j = 0; j < size; j++) {
                        blocks.set(indexOfEmptySpaceForFile + j, new DiskBlock.File(id));
                    }
                    moved = true;
                    break;
                }
            }

            if (moved) {
                for (int i = 0; i < size; i++) {
                    blocks.set(offsetOfFileToMove + i, DiskBlock.empty());
                }
            }
        }

        void print() {
            for (int i = 0; i < blocks.size(); i++) {
                final var id = blocks.get(i).id();
                if (id == -1) {
                    System.out.print(".");
                } else {
                    System.out.printf("%d", id);
                }
            }
            System.out.println();
        }

        static Disk fromDenseFormat(String input) {
            final var diskBlocks = new ArrayList<DiskBlock>();

            int fileId = 0;
            for (int denseBlockIndex = 0; denseBlockIndex < input.length(); denseBlockIndex++) {
                char c = input.charAt(denseBlockIndex);
                int size = Integer.parseInt(String.valueOf(c));

                if ((denseBlockIndex % 2) != 0) {
                    for (int i = 0; i < size; i++) {
                        diskBlocks.add(DiskBlock.empty());
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        diskBlocks.add(new DiskBlock.File(fileId));
                    }
                    fileId++;
                }
            }

            return new Disk(diskBlocks);
        }
    }

    sealed interface DiskBlock {
        DiskBlock EMPTY = new DiskBlock.Empty();

        long id();

        record File(long id) implements DiskBlock {
        }

        final class Empty implements DiskBlock {
            private Empty() {
            }

            public long id() {
                return -1;
            }
        }

        static DiskBlock empty() {
            return EMPTY;
        }
    }
}
