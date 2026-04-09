package com.project.bst;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BinarySearchTree Tests")
public class BinarySearchTreeTest {


    private BinarySearchTree bst;

    @BeforeEach
    void setUp() {
        // VALIDATE must be true when running tests, false during benchmarking

        bst = new BinarySearchTree(true);
    }

    // 1. Insert

    @Test
    @DisplayName("Insert into empty tree returns true")
    void testInsertIntoEmptyTree() {
        assertTrue(bst.insert(10));
    }

    @Test
    @DisplayName("Insert duplicate returns false and does not change size")
    void testInsertDuplicate() {
        bst.insert(10);
        assertFalse(bst.insert(10));
        assertEquals(1, bst.size());
    }

    @Test
    @DisplayName("Insert multiple distinct values increases size correctly")
    void testInsertMultiple() {
        int[] values = {5, 3, 7, 1, 4, 6, 8};
        for (int v : values) assertTrue(bst.insert(v));
        assertEquals(values.length, bst.size());
    }

    @Test
    @DisplayName("Insert ascending sequence (worst-case skewed tree)")
    void testInsertAscending() {
        for (int i = 1; i <= 100; i++) bst.insert(i);
        assertEquals(100, bst.size());
        assertEquals(99, bst.height()); // fully right-skewed
    }

    @Test
    @DisplayName("Insert descending sequence (worst-case skewed tree)")
    void testInsertDescending() {
        for (int i = 100; i >= 1; i--) bst.insert(i);
        assertEquals(100, bst.size());
        assertEquals(99, bst.height()); // fully left-skewed
    }

    // 2. Contains

    @Test
    @DisplayName("Contains returns false on empty tree")
    void testContainsOnEmptyTree() {
        assertFalse(bst.contains(5));
    }

    @Test
    @DisplayName("Contains returns true for inserted values")
    void testContainsPresentValues() {
        int[] values = {10, 5, 15, 3, 7};
        for (int v : values) bst.insert(v);
        for (int v : values) assertTrue(bst.contains(v), "Should contain " + v);
    }

    @Test
    @DisplayName("Contains returns false for absent values")
    void testContainsAbsentValues() {
        bst.insert(10);
        bst.insert(20);
        assertFalse(bst.contains(15));
        assertFalse(bst.contains(0));
    }

    // 3. Delete

    @Test
    @DisplayName("Delete from empty tree returns false")
    void testDeleteFromEmptyTree() {
        assertFalse(bst.delete(5));
    }

    @Test
    @DisplayName("Delete absent value returns false")
    void testDeleteAbsentValue() {
        bst.insert(10);
        assertFalse(bst.delete(99));
        assertEquals(1, bst.size());
    }

    @Test
    @DisplayName("Delete leaf node")
    void testDeleteLeaf() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        assertTrue(bst.delete(5));
        assertFalse(bst.contains(5));
        assertEquals(2, bst.size());
    }

    @Test
    @DisplayName("Delete node with one child")
    void testDeleteNodeWithOneChild() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(3); // left child of 5
        assertTrue(bst.delete(5));
        assertFalse(bst.contains(5));
        assertTrue(bst.contains(3));
        assertEquals(2, bst.size());
    }

    @Test
    @DisplayName("Delete node with two children")
    void testDeleteNodeWithTwoChildren() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        assertTrue(bst.delete(5)); // has two children: 3 and 7
        assertFalse(bst.contains(5));
        assertTrue(bst.contains(3));
        assertTrue(bst.contains(7));
        assertEquals(4, bst.size());
    }

    @Test
    @DisplayName("Delete root node")
    void testDeleteRoot() {
        bst.insert(10);
        assertTrue(bst.delete(10));
        assertFalse(bst.contains(10));
        assertEquals(0, bst.size());
    }

    @Test
    @DisplayName("Delete only element leaves empty tree")
    void testDeleteOnlyElement() {
        bst.insert(42);
        assertTrue(bst.delete(42));
        assertEquals(0, bst.size());
        assertEquals(-1, bst.height());
    }

    @Test
    @DisplayName("Delete all elements one by one maintains BST invariant")
    void testDeleteAllElements() {
        int[] values = {10, 5, 15, 3, 7, 12, 20};
        for (int v : values) bst.insert(v);
        for (int v : values) {
            assertTrue(bst.delete(v));
        }
        assertEquals(0, bst.size());
    }

    // 4. inOrder

    @Test
    @DisplayName("inOrder returns empty array for empty tree")
    void testInOrderEmpty() {
        assertArrayEquals(new int[]{}, bst.inOrder());
    }

    @Test
    @DisplayName("inOrder returns single element for single-node tree")
    void testInOrderSingleElement() {
        bst.insert(42);
        assertArrayEquals(new int[]{42}, bst.inOrder());
    }

    @Test
    @DisplayName("inOrder returns sorted array after random insertions")
    void testInOrderSorted() {
        int[] values = {5, 3, 8, 1, 4, 7, 9};
        for (int v : values) bst.insert(v);
        int[] result = bst.inOrder();
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i - 1] < result[i],
                    "inOrder not sorted at index " + i + ": " + result[i-1] + " >= " + result[i]);
        }
    }

    @Test
    @DisplayName("inOrder returns sorted array after insertions and deletions")
    void testInOrderAfterDeletions() {
        for (int v : new int[]{10, 5, 15, 3, 7, 12, 20}) bst.insert(v);
        bst.delete(5);
        bst.delete(15);
        int[] result = bst.inOrder();
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i - 1] < result[i], "inOrder not sorted after deletions");
        }
        assertEquals(5, result.length);
    }

    // 5. Height

    @Test
    @DisplayName("Height of empty tree is -1")
    void testHeightEmpty() {
        assertEquals(-1, bst.height());
    }

    @Test
    @DisplayName("Height of single-node tree is 0")
    void testHeightSingleNode() {
        bst.insert(1);
        assertEquals(0, bst.height());
    }

    @Test
    @DisplayName("Height of balanced tree is correct")
    void testHeightBalanced() {
        // Insert in level-order to get a perfect tree of height 2
        for (int v : new int[]{4, 2, 6, 1, 3, 5, 7}) bst.insert(v);
        assertEquals(2, bst.height());
    }

    // 6. Size

    @Test
    @DisplayName("Size starts at 0")
    void testSizeInitial() {
        assertEquals(0, bst.size());
    }

    @Test
    @DisplayName("Size increments on insert and decrements on delete")
    void testSizeUpdates() {
        bst.insert(1);
        bst.insert(2);
        assertEquals(2, bst.size());
        bst.delete(1);
        assertEquals(1, bst.size());
        bst.delete(2);
        assertEquals(0, bst.size());
    }

    @Test
    @DisplayName("Size does not change on duplicate insert or absent delete")
    void testSizeUnchangedOnNoOp() {
        bst.insert(5);
        bst.insert(5);   // duplicate
        assertEquals(1, bst.size());
        bst.delete(99);  // not present
        assertEquals(1, bst.size());
    }

    // 7. Structural validation (BST property exhaustive)

    @Test
    @DisplayName("BST property holds after 1000 random insertions and deletions")
    void testStructuralValidityUnderStress() {
        java.util.Random rng = new java.util.Random(42L);
        java.util.Set<Integer> inserted = new java.util.HashSet<>();

        for (int i = 0; i < 1000; i++) {
            int v = rng.nextInt(2000);
            bst.insert(v);
            inserted.add(v);
        }

        // Delete ~half
        java.util.List<Integer> list = new java.util.ArrayList<>(inserted);
        java.util.Collections.shuffle(list, rng);
        for (int i = 0; i < list.size() / 2; i++) {
            bst.delete(list.get(i));
        }
    }
}