package gol.world.neo;

public class ByteArrLine implements Line, Segmented {

	public byte[] arr = null;
	public int arrOffset = 0;

	@Override
	public void set(int i) {
		if (arr == null) {
			arr = new byte[1];
			arrOffset = byteIndex(i);
		} else
			extendIfNeeded(i);

		int arrIndx = arrIndex(i);
		arr[arrIndx] |= maskOf(i);
		adjustMargin(arrIndx);
		adjustMargin(arrIndx - 1);
		adjustMargin(arrIndx + 1);
	}

	private void adjustMargin(int arrIndx) {
		boolean isInRange = arrIndx >= 0 && arrIndx < arr.length;
		boolean isMoreThanFirst = arrIndx > 0;
		boolean isLessThanLast = arrIndx + 1 < arr.length;

		if (isMoreThanFirst && isInRange
				&& (arr[arrIndx - 1] & (0b01000000)) != 0) {
			arr[arrIndx] |= 0b00000001;
		}

		if (isLessThanLast && isInRange
				&& (arr[arrIndx + 1] & (0b00000010)) != 0) {
			arr[arrIndx] |= 0b10000000;
		}
	}

	public void extendIfNeeded(int i) {
		int arrIndx = arrIndex(i);

		if (arrIndx < 0)
			prependBytes(arrIndx);
		else if (arrIndx >= arr.length)
			appendBytes(arrIndx);
	}

	private void prependBytes(int arrIndx) {
		byte[] newArr = new byte[arr.length - arrIndx];

		for (int j = 0; j < arr.length; j++)
			newArr[j - arrIndx] = arr[j];

		arrOffset += arrIndx;
		arr = newArr;
	}

	private void appendBytes(int arrIndx) {
		byte[] newArr = new byte[arrIndx + 1];

		for (int j = 0; j < arr.length; j++)
			newArr[j] = arr[j];

		arr = newArr;
	}

	public int byteIndex(int i) {
		return (i < 0 ? i - 5 : i) / 6;
	}

	public int bitIndexOf(int i) {
		return i < 0 ? 6 + ((i + 1) % 6) : i % 6 + 1;
	}

	@Override
	public boolean isSet(int i) {
		return (byteOf(i) & maskOf(i)) != 0;
	}

	private byte byteOf(int i) {
		return arr[arrIndex(i)];
	}

	private int arrIndex(int i) {
		return byteIndex(i) - arrOffset;
	}

	private int maskOf(int i) {
		return 1 << bitIndexOf(i);
	}

	@Override
	public int minIndex() {
		return arr == null? 0 : arrOffset;
	}

	@Override
	public int maxIndex() {
		return arr == null? 0 : arrOffset + arr.length -1;
	}

	@Override
	public byte getByte(int index) {
		int arrIndex = index - arrOffset;
		boolean isInRange = ((arrIndex >= minIndex()) && (arrIndex <= maxIndex()));
		boolean isDefined = arr != null;
		return isInRange && isDefined ? arr[arrIndex]: 0;
	}

	@Override
	public boolean hasBytes() {
		return arr != null;
	}
}
