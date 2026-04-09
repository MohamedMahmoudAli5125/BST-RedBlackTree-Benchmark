package com.project.rbtree;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RedBlackTree Tests")
public class RedBlackTreeTest {

    // VALIDATE must be true when running tests, false during benchmarking
    static final boolean VALIDATE = true;

    private RedBlackTree rbt;

    @BeforeEach
    void setUp() {
        rbt = new RedBlackTree();
    }

    // 1. Insert

    @Test
    @DisplayName("Insert into empty tree returns true")
    void testInsertIntoEmptyTree() {
        assertTrue(rbt.insert(10));
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Insert duplicate returns false and does not change size")
    void testInsertDuplicate() {
        rbt.insert(10);
        assertFalse(rbt.insert(10));
        assertEquals(1, rbt.size());
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Insert multiple distinct values increases size correctly")
    void testInsertMultiple() {
        int[] values = {5, 3, 7, 1, 4, 6, 8};
        for (int v : values) assertTrue(rbt.insert(v));
        assertEquals(values.length, rbt.size());
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Insert ascending sequence triggers rotations and recoloring")
    void testInsertAscendingSequence() {
        for (int i = 1; i <= 100; i++) rbt.insert(i);
        assertEquals(100, rbt.size());
        // RBT guarantees height <= 2*log2(n+1); for n=100 that is ~13
        assertTrue(rbt.height() <= 14,
                "Height " + rbt.height() + " exceeds RBT upper bound for 100 nodes");
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Insert descending sequence triggers rotations and recoloring")
    void testInsertDescendingSequence() {
        for (int i = 100; i >= 1; i--) rbt.insert(i);
        assertEquals(100, rbt.size());
        assertTrue(rbt.height() <= 14,
                "Height " + rbt.height() + " exceeds RBT upper bound for 100 nodes");
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Root is always BLACK after any insertion")
    void testRootIsAlwaysBlack() {
        for (int v : new int[]{10, 5, 15, 3, 7, 12, 20}) {
            rbt.insert(v);
            assertEquals(Color.BLACK, rbt.getRoot().color,
                    "Root must be BLACK after inserting " + v);
        }
    }

    // 2. Contains

    @Test
    @DisplayName("Contains returns false on empty tree")
    void testContainsOnEmptyTree() {
        assertFalse(rbt.contains(5));
    }

    @Test
    @DisplayName("Contains returns true for all inserted values")
    void testContainsPresentValues() {
        int[] values = {10, 5, 15, 3, 7};
        for (int v : values) rbt.insert(v);
        for (int v : values) assertTrue(rbt.contains(v), "Should contain " + v);
    }

    @Test
    @DisplayName("Contains returns false for absent values")
    void testContainsAbsentValues() {
        rbt.insert(10);
        rbt.insert(20);
        assertFalse(rbt.contains(15));
        assertFalse(rbt.contains(0));
    }

    // 3. Delete

    @Test
    @DisplayName("Delete from empty tree returns false")
    void testDeleteFromEmptyTree() {
        assertFalse(rbt.delete(5));
    }

    @Test
    @DisplayName("Delete absent value returns false")
    void testDeleteAbsentValue() {
        rbt.insert(10);
        assertFalse(rbt.delete(99));
        assertEquals(1, rbt.size());
    }

    @Test
    @DisplayName("Delete a RED leaf node")
    void testDeleteRedLeaf() {
        // 10(B) -> 5(R), 15(R) — deleting 5 removes a red leaf
        rbt.insert(10);
        rbt.insert(5);
        rbt.insert(15);
        assertTrue(rbt.delete(5));
        assertFalse(rbt.contains(5));
        assertEquals(2, rbt.size());
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Delete a BLACK node triggers fixup")
    void testDeleteBlackNodeTriggersFixup() {
        // Build a tree where deleting a node forces deleteFixup
        for (int v : new int[]{10, 5, 15, 3, 7, 12, 20}) rbt.insert(v);
        assertTrue(rbt.delete(3)); // likely BLACK — forces rebalancing
        assertFalse(rbt.contains(3));
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Delete root node")
    void testDeleteRoot() {
        rbt.insert(10);
        assertTrue(rbt.delete(10));
        assertFalse(rbt.contains(10));
        assertEquals(0, rbt.size());
    }

    @Test
    @DisplayName("Delete only element leaves empty tree")
    void testDeleteOnlyElement() {
        rbt.insert(42);
        assertTrue(rbt.delete(42));
        assertEquals(0, rbt.size());
        assertEquals(-1, rbt.height());
    }

    @Test
    @DisplayName("Delete node with two children")
    void testDeleteNodeWithTwoChildren() {
        for (int v : new int[]{10, 5, 15, 3, 7, 12, 20}) rbt.insert(v);
        assertTrue(rbt.delete(10)); // root has two children
        assertFalse(rbt.contains(10));
        assertEquals(6, rbt.size());
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Root is always BLACK after any deletion")
    void testRootIsAlwaysBlackAfterDelete() {
        int[] values = {10, 5, 15, 3, 7, 12, 20};
        for (int v : values) rbt.insert(v);
        for (int v : values) {
            rbt.delete(v);
            if (rbt.size() > 0) {
                assertEquals(Color.BLACK, rbt.getRoot().color,
                        "Root must be BLACK after deleting " + v);
            }
        }
    }

    @Test
    @DisplayName("Delete all elements one by one maintains all RBT invariants")
    void testDeleteAllElementsMaintainsInvariants() {
        int[] values = {10, 5, 15, 3, 7, 12, 20, 1, 4, 6, 8};
        for (int v : values) rbt.insert(v);
        for (int v : values) {
            assertTrue(rbt.delete(v));
            if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
        }
        assertEquals(0, rbt.size());
    }

    // 4. inOrder

    @Test
    @DisplayName("inOrder returns empty array for empty tree")
    void testInOrderEmpty() {
        assertArrayEquals(new int[]{}, rbt.inOrder());
    }

    @Test
    @DisplayName("inOrder returns single element for single-node tree")
    void testInOrderSingleElement() {
        rbt.insert(42);
        assertArrayEquals(new int[]{42}, rbt.inOrder());
    }

    @Test
    @DisplayName("inOrder returns sorted array after random insertions")
    void testInOrderSorted() {
        int[] values = {5, 3, 8, 1, 4, 7, 9};
        for (int v : values) rbt.insert(v);
        int[] result = rbt.inOrder();
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i - 1] < result[i],
                    "inOrder not sorted at index " + i);
        }
    }

    @Test
    @DisplayName("inOrder returns sorted array after insertions and deletions")
    void testInOrderAfterDeletions() {
        for (int v : new int[]{10, 5, 15, 3, 7, 12, 20}) rbt.insert(v);
        rbt.delete(5);
        rbt.delete(15);
        int[] result = rbt.inOrder();
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i - 1] < result[i], "inOrder not sorted after deletions");
        }
        assertEquals(5, result.length);
    }

    // 5. Height

    @Test
    @DisplayName("Height of empty tree is -1")
    void testHeightEmpty() {
        assertEquals(-1, rbt.height());
    }

    @Test
    @DisplayName("Height of single-node tree is 0")
    void testHeightSingleNode() {
        rbt.insert(1);
        assertEquals(0, rbt.height());
    }

    @Test
    @DisplayName("Height stays within RBT bound 2*log2(n+1) for large n")
    void testHeightBound() {
        int n = 1000;
        for (int i = 1; i <= n; i++) rbt.insert(i);
        int maxAllowed = 2 * (int) (Math.log(n + 1) / Math.log(2)) + 1;
        assertTrue(rbt.height() <= maxAllowed,
                "Height " + rbt.height() + " exceeds theoretical RBT bound " + maxAllowed);
    }

    // 6. Size

    @Test
    @DisplayName("Size starts at 0")
    void testSizeInitial() {
        assertEquals(0, rbt.size());
    }

    @Test
    @DisplayName("Size increments on insert and decrements on delete")
    void testSizeUpdates() {
        rbt.insert(1);
        rbt.insert(2);
        assertEquals(2, rbt.size());
        rbt.delete(1);
        assertEquals(1, rbt.size());
        rbt.delete(2);
        assertEquals(0, rbt.size());
    }

    @Test
    @DisplayName("Size does not change on duplicate insert or absent delete")
    void testSizeUnchangedOnNoOp() {
        rbt.insert(5);
        rbt.insert(5);
        assertEquals(1, rbt.size());
        rbt.delete(99);
        assertEquals(1, rbt.size());
    }

    // 7. RBT-specific structural validation under stress

    @Test
    @DisplayName("All RBT invariants hold after 1000 random insertions")
    void testStructuralValidityAfterInserts() {
        java.util.Random rng = new java.util.Random(42L);
        for (int i = 0; i < 1000; i++) rbt.insert(rng.nextInt(2000));
        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("All RBT invariants hold after 1000 random insertions and ~500 deletions")
    void testStructuralValidityAfterInsertsAndDeletes() {
        java.util.Random rng = new java.util.Random(42L);
        java.util.Set<Integer> inserted = new java.util.HashSet<>();
        for (int i = 0; i < 1000; i++) {
            int v = rng.nextInt(2000);
            rbt.insert(v);
            inserted.add(v);
        }

        java.util.List<Integer> list = new java.util.ArrayList<>(inserted);
        java.util.Collections.shuffle(list, rng);
        for (int i = 0; i < list.size() / 2; i++) rbt.delete(list.get(i));

        if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
    }

    @Test
    @DisplayName("Black-height is uniform after alternating inserts and deletes")
    void testBlackHeightUniformAfterMixedOps() {
        java.util.Random rng = new java.util.Random(7L);
        java.util.List<Integer> present = new java.util.ArrayList<>();

        for (int i = 0; i < 200; i++) {
            if (!present.isEmpty() && rng.nextBoolean()) {
                int idx = rng.nextInt(present.size());
                rbt.delete(present.remove(idx));
            } else {
                int v = rng.nextInt(500);
                if (rbt.insert(v)) present.add(v);
            }
            if (VALIDATE) RBTValidator.check(rbt.getRoot(), rbt.getT_nil(), rbt.size());
        }
    }
}