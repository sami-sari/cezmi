package com.samisari.cezmi.animator.sound.reading.syllabus;

public class SyllabusGenerator {
	public static final String	alphabet[]			= { "a", "b", "c", "ç", "d", "e", "f", "g", "ğ", "h", "ı", "i", "j", "k", "l", "m", "n",
			"o", "ö", "p", "r", "s", "ş", "t", "u", "ü", "v", "y", "z" };
	public static final String	wovels[]			= { "a", "e", "ı", "i", "o", "ö", "u", "ü" };
	public static final String	non_wovels[]		= { "b", "c", "ç", "d", "f", "g", "ğ", "h", "j", "k", "l", "m", "n", "p", "r", "s", "ş",
			"t", "v", "y", "z" };
	public static String		sylabus1Letters[]	= new String[wovels.length];
	public static String		sylabus2Letters[]	= new String[wovels.length * non_wovels.length * 2];
	public static String		sylabus3Letters[]	= new String[non_wovels.length * wovels.length * non_wovels.length];
	public static String		sylabus4Letters[]	= new String[wovels.length * sylabus3Letters.length * 2];
	public static String		sylabus5Letters[]	= new String[wovels.length * sylabus3Letters.length * wovels.length];

	static {
		build1Letters();
		build2Letters();
		build3Letters();
		build4Letters();
		build5Letters();
	}
	
	private static void build5Letters() {
		// TODO Auto-generated method stub

	}

	private static void build4Letters() {
		// TODO Auto-generated method stub

	}

	private static void build3Letters() {
		for (int h = 0; h < non_wovels.length; h++) {
			for (int i = 0; i < wovels.length; i++) {
				for (int j = 0; i < non_wovels.length; j++) {
					sylabus3Letters[i * j + j] = wovels[i] + non_wovels[j];
					sylabus3Letters[wovels.length * non_wovels.length + i * j + j] = non_wovels[j] + wovels[i];
				}
			}
		}
	}

	private static void build2Letters() {
		for (int i = 0; i < wovels.length; i++) {
			for (int j = 0; i < non_wovels.length; j++) {
				sylabus2Letters[i * j + j] = wovels[i] + non_wovels[j];
				sylabus2Letters[wovels.length * non_wovels.length + i * j + j] = non_wovels[j] + wovels[i];
			}
		}

	}

	private static void build1Letters() {
		for (int i = 0; i < wovels.length; i++) {
			sylabus1Letters[i] = wovels[i];
		}
	}

	public static void main(String[] args) {
		new SyllabusGenerator();
	}

	public SyllabusGenerator() {
		
	}
}