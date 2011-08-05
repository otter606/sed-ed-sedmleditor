package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocalRGBTest {
    LocalRGB rgb;
    private final int RED = 1;
    private final int GREEN = 2;
    private final int BLUE = 3;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		rgb = createASpecificRGB();
		LocalRGB other = createASpecificRGB();
		assertEquals(rgb.hashCode(), other.hashCode());
	
	}

	@Test
	public void testGetRed() {
		rgb = createASpecificRGB();
		assertEquals(RED, rgb.getRed());
	}

	@Test
	public void testGetGreen() {
		rgb = createASpecificRGB();
		assertEquals(GREEN, rgb.getGreen());
	}

	@Test
	public void testGetBlue() {
		rgb = createASpecificRGB();
		assertEquals(BLUE, rgb.getBlue());
	}

	@Test
	public void testLocalRGBIntIntInt() {
		rgb = createASpecificRGB();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLocalRGBIntIntIntCheckRed() {
		rgb = new LocalRGB(-1,0,0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLocalRGBIntIntIntCheckRedValidLessThan255() {
		rgb = new LocalRGB(270,0,0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLocalRGBIntIntIntCheckGreen() {
		rgb = new LocalRGB(0,-1,0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLocalRGBIntIntIntCheckGreenValidMoreThan0() {
		rgb = new LocalRGB(0,260,-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLocalRGBIntIntIntCheckBlue() {
		rgb = new LocalRGB(0,0,-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLocalRGBIntIntIntCheckBlueValidLessThan255() {
		rgb = new LocalRGB(0,0,260);
	}

	private LocalRGB createASpecificRGB() {
		return new LocalRGB(RED,GREEN,BLUE);
	}

	@Test
	public void testToString() {
		
	}

	@Test
	public void testFromString() {
		rgb = LocalRGB.fromString("1,2,3");
		assertEquals(createASpecificRGB(), rgb);
	}

	@Test
	public void testEqualsObject() {
		rgb = createASpecificRGB();
		LocalRGB other = createASpecificRGB();
		assertEquals(rgb, other);
		assertFalse(rgb==other);
	}
	
	@Test
	public void assertConstantsAreSingleInstances() {
		assertTrue(LocalRGB.WHITE == LocalRGB.WHITE);
	}

}
