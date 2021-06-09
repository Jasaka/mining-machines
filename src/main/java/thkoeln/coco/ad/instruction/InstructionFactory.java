package thkoeln.coco.ad.instruction;

import thkoeln.coco.ad.miningMachine.MiningMachineException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionFactory {

    public static <T extends Instruction> T getInstruction(String inputString) {
        String badCharacterRegEx = "[^a-z0-9-,\\[\\]()]";
        String movementRegEx = "^\\[(no|ea|we|so){1},[0-9]{1,9}\\]$";
        String uuidRegEx = "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}";
        String transportRegEx = "^\\[{1}tr{1}," + uuidRegEx + "\\]{1}$";
        String entryRegEx = "^\\[{1}en{1}," + uuidRegEx + "\\]{1}$";
        String coordinateRegEx = "\\([0-9]{1,9},[0-9]{1,9}\\)";
        String barrierRegEx = "^" + coordinateRegEx + "-" + coordinateRegEx + "$";
        String squareRegEx ="^" + coordinateRegEx + "$";

        if (!inputString.isEmpty() && !isStringMatchingRegEx(inputString, badCharacterRegEx)) {
            if (isStringMatchingRegEx(inputString, movementRegEx)) {
                return (T) new MoveInstruction(inputString);
            }
            if (isStringMatchingRegEx(inputString, transportRegEx)) {
                return (T) new TransportInstruction(inputString);
            }
            if (isStringMatchingRegEx(inputString, entryRegEx)) {
                return (T) new EntryInstruction(inputString);
            }
            if (isStringMatchingRegEx(inputString, barrierRegEx)) {
                return (T) new BarrierInstruction(inputString);
            }
            if (isStringMatchingRegEx(inputString, squareRegEx)) {
                return (T) new CoordinateInstruction(inputString);
            }
            throw new MiningMachineException("Did not provide valid Input String");
        } else throw new MiningMachineException("Provided Empty Input String or Bad Characters");
    }

    private static boolean isStringMatchingRegEx(String stringToMatch, String regEx) {
        Pattern regex = Pattern.compile(regEx);
        Matcher matcher = regex.matcher(stringToMatch);
        return matcher.matches();
    }
}
