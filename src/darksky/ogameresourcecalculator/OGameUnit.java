package darksky.ogameresourcecalculator;

import java.util.Arrays;
import java.util.Objects;

import darksky.ogameresourcecalculator.OGameUnit.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * OGameUnit is a buildable unit in OGame
 * 
 * @author ogame.de Dark Sky
 * 
 */
public class OGameUnit {

	public static final ObservableList<OGameUnit> UNIT_LIST = FXCollections.observableArrayList();

	static {
		UNIT_LIST.add(new OGameUnit(1, 60, 15, 0, 1.5, Type.BUILDING)); // metmine
		UNIT_LIST.add(new OGameUnit(2, 48, 24, 0, 1.6, Type.BUILDING)); // krismine
		UNIT_LIST.add(new OGameUnit(3, 225, 75, 0, 1.5, Type.BUILDING)); // deutsynth
		UNIT_LIST.add(new OGameUnit(4, 75, 30, 0, 1.5, Type.BUILDING)); // solarkraft
		UNIT_LIST.add(new OGameUnit(12, 900, 360, 180, 1.8, Type.BUILDING)); // fusi
		UNIT_LIST.add(new OGameUnit(14, 400, 120, 200, 2.0, Type.BUILDING)); // robo
		UNIT_LIST.add(new OGameUnit(15, 1000000, 500000, 100000, 2.0, Type.BUILDING)); // nani
		UNIT_LIST.add(new OGameUnit(21, 400, 200, 100, 2.0, Type.BUILDING)); // werft
		UNIT_LIST.add(new OGameUnit(22, 2000, 0, 0, 2.0, Type.BUILDING)); // metspeicher
		UNIT_LIST.add(new OGameUnit(23, 2000, 1000, 0, 2.0, Type.BUILDING)); // krisspeicher
		UNIT_LIST.add(new OGameUnit(24, 2000, 2000, 0, 2.0, Type.BUILDING)); // deutspeicher
		UNIT_LIST.add(new OGameUnit(25, 0, 0, 0, 2.0, Type.BUILDING)); // metversteck
		UNIT_LIST.add(new OGameUnit(26, 0, 0, 0, 2.0, Type.BUILDING)); // krisversteck
		UNIT_LIST.add(new OGameUnit(27, 0, 0, 0, 2.0, Type.BUILDING)); // deutversteck
		UNIT_LIST.add(new OGameUnit(31, 200, 400, 200, 2.0, Type.BUILDING)); // labor
		UNIT_LIST.add(new OGameUnit(33, 900, 50000, 100000, 2.0, Type.BUILDING)); // terra
		UNIT_LIST.add(new OGameUnit(34, 20000, 40000, 0, 2.0, Type.BUILDING)); // allydepot
		UNIT_LIST.add(new OGameUnit(41, 20000, 40000, 20000, 2.0, Type.BUILDING)); // mondbasis
		UNIT_LIST.add(new OGameUnit(42, 20000, 40000, 20000, 2.0, Type.BUILDING)); // phalanx
		UNIT_LIST.add(new OGameUnit(43, 2000000, 4000000, 2000000, 2.0, Type.BUILDING)); // sprungtor
		UNIT_LIST.add(new OGameUnit(44, 20000, 20000, 1000, 2.0, Type.BUILDING)); // raksilo

		UNIT_LIST.add(new OGameUnit(106, 200, 1000, 200, 2.0, Type.RESEARCH)); // spiotech
		UNIT_LIST.add(new OGameUnit(108, 0, 400, 600, 2.0, Type.RESEARCH)); // compu
		UNIT_LIST.add(new OGameUnit(109, 800, 200, 0, 2.0, Type.RESEARCH)); // waffen
		UNIT_LIST.add(new OGameUnit(110, 200, 600, 0, 2.0, Type.RESEARCH)); // schild
		UNIT_LIST.add(new OGameUnit(111, 1000, 0, 0, 2.0, Type.RESEARCH)); // panzer
		UNIT_LIST.add(new OGameUnit(113, 0, 800, 400, 2.0, Type.RESEARCH)); // energie
		UNIT_LIST.add(new OGameUnit(114, 0, 4000, 2000, 2.0, Type.RESEARCH)); // hypertechnik
		UNIT_LIST.add(new OGameUnit(115, 400, 0, 600, 2.0, Type.RESEARCH)); // verbrenner
		UNIT_LIST.add(new OGameUnit(117, 2000, 4000, 600, 2.0, Type.RESEARCH)); // impuls
		UNIT_LIST.add(new OGameUnit(118, 10000, 20000, 6000, 2.0, Type.RESEARCH)); // hyperantrieb
		UNIT_LIST.add(new OGameUnit(120, 200, 100, 0, 2.0, Type.RESEARCH)); // laser
		UNIT_LIST.add(new OGameUnit(121, 1000, 300, 100, 2.0, Type.RESEARCH)); // ion
		UNIT_LIST.add(new OGameUnit(122, 2000, 4000, 1000, 2.0, Type.RESEARCH)); // plasma
		UNIT_LIST.add(new OGameUnit(123, 240000, 400000, 160000, 2.0, Type.RESEARCH)); // igfn
		UNIT_LIST.add(new OGameUnit(124, 4000, 8000, 4000, 1.75, Type.RESEARCH)); // astro
		UNIT_LIST.add(new OGameUnit(199, 0, 0, 0, 3, Type.RESEARCH)); // gravi

		UNIT_LIST.add(new OGameUnit(202, 2000, 2000, 0, 1.0, Type.UNIT)); // kt
		UNIT_LIST.add(new OGameUnit(203, 6000, 6000, 0, 1.0, Type.UNIT)); // gt
		UNIT_LIST.add(new OGameUnit(204, 3000, 1000, 0, 1.0, Type.UNIT)); // lj
		UNIT_LIST.add(new OGameUnit(205, 6000, 4000, 0, 1.0, Type.UNIT)); // sj
		UNIT_LIST.add(new OGameUnit(206, 20000, 7000, 2000, 1.0, Type.UNIT)); // xer
		UNIT_LIST.add(new OGameUnit(207, 45000, 15000, 180, 1.0, Type.UNIT)); // ss
		UNIT_LIST.add(new OGameUnit(208, 10000, 20000, 10000, 1.0, Type.UNIT)); // kolo
		UNIT_LIST.add(new OGameUnit(209, 10000, 6000, 2000, 1.0, Type.UNIT)); // rec
		UNIT_LIST.add(new OGameUnit(210, 0, 1000, 0, 1.0, Type.UNIT)); // spio
		UNIT_LIST.add(new OGameUnit(211, 50000, 25000, 15000, 1.0, Type.UNIT)); // bb
		UNIT_LIST.add(new OGameUnit(212, 0, 2000, 500, 1.0, Type.UNIT)); // sat
		UNIT_LIST.add(new OGameUnit(213, 60000, 50000, 15000, 1.0, Type.UNIT)); // zer
		UNIT_LIST.add(new OGameUnit(214, 5000000, 4000000, 1000000, 1.0, Type.UNIT)); // rip
		UNIT_LIST.add(new OGameUnit(215, 30000, 40000, 15000, 1.0, Type.UNIT)); // sxer

		UNIT_LIST.add(new OGameUnit(401, 2000, 0, 0, 1.0, Type.UNIT)); // rak
		UNIT_LIST.add(new OGameUnit(402, 1500, 500, 0, 1.0, Type.UNIT)); // ll
		UNIT_LIST.add(new OGameUnit(403, 6000, 2000, 0, 1.0, Type.UNIT)); // sl
		UNIT_LIST.add(new OGameUnit(404, 20000, 15000, 2000, 1.0, Type.UNIT)); // gauss
		UNIT_LIST.add(new OGameUnit(405, 2000, 6000, 180, 1.0, Type.UNIT)); // ion
		UNIT_LIST.add(new OGameUnit(406, 50000, 50000, 30000, 1.0, Type.UNIT)); // plasma
		UNIT_LIST.add(new OGameUnit(407, 10000, 10000, 0, 1.0, Type.UNIT)); // klschild
		UNIT_LIST.add(new OGameUnit(408, 50000, 50000, 0, 1.0, Type.UNIT)); // grschild
		UNIT_LIST.add(new OGameUnit(502, 8000, 0, 2000, 1.0, Type.UNIT)); // arak
		UNIT_LIST.add(new OGameUnit(503, 12500, 2500, 10000, 1.0, Type.UNIT)); // irak
	}

	public enum Type {
		BUILDING, RESEARCH, UNIT
	};

	/**
	 * id used in OGame
	 */
	public int id = 0;

	/**
	 * if type is Type.BUILDING or Type.RESEARCH this is the cost of level 1 if
	 * type is Type.UNIT this is the cost of one unit; resource order metal
	 * crystal deuterium
	 */
	public long[] baseCosts = { 0, 0, 0 };

	/**
	 * if type is Type.BUILDING or Type.RESEARCH this is a cost multiplier for
	 * each level
	 */
	public double factor = 1;

	public Type type = Type.BUILDING;

	public OGameUnit() {
	}

	public OGameUnit(int id, long baseMet, long baseKris, long baseDeut, double factor, Type type) {
		this.id = id;
		this.baseCosts[0] = baseMet;
		this.baseCosts[1] = baseKris;
		this.baseCosts[2] = baseDeut;
		this.factor = factor;
		this.type = type;
	}

	@Override
	public String toString() {
		return "OGameUnit [id=" + id + ", baseCosts=" + Arrays.toString(baseCosts) + ", factor=" + factor + ", type="
				+ type + "]";
	}
	
	public static long[] getCosts(OGameUnit u, long level) {
		Objects.requireNonNull(u, "getCosts OGameUnit is null");

		double[] costs = { 0, 0, 0 };
		long[] retCosts = { 0, 0, 0 };
		for (int i = 0; i < 3; i++) {
			if (u.type == Type.UNIT)
				costs[i] = u.baseCosts[i] * level;
			else
				costs[i] = u.baseCosts[i] * Math.pow(u.factor, Math.max(0, level) - 1);
			retCosts[i] = Math.max(0, (long) costs[i]);
		}
		return retCosts;
	}
}
