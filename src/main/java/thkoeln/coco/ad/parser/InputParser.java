package thkoeln.coco.ad.parser;

import thkoeln.coco.ad.instruction.*;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitives.Square;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    public Instruction parseInput(String inputToParse) {
        String badCharacterRegEx = "[^a-z0-9-,\\[\\]()]";
        String machineCommandRegEx = "^\\[.*\\]$";
        String barrierCommandRegEx = "^\\({1}[0-9]{1,9},[0-9]{1,9}\\)-\\([0-9]{1,9},[0-9]{1,9}\\){1}$";
        if (!isStringMatchingRegEx(inputToParse, badCharacterRegEx)) {
            if (isStringMatchingRegEx(inputToParse, machineCommandRegEx)) {
                return parseValidMachineCommand(inputToParse);
            } else if (isStringMatchingRegEx(inputToParse, barrierCommandRegEx)) {
                return parseBarrierCreation(inputToParse);
            }
        } else throw new MiningMachineException("Found Illegal Characters In Instruction!");
        throw new MiningMachineException("No known command found!");
    }

    private static boolean isStringMatchingRegEx(String stringToMatch, String regEx) {
        Pattern regex = Pattern.compile(regEx);
        Matcher matcher = regex.matcher(stringToMatch);
        return matcher.matches();
    }

    private Instruction parseValidMachineCommand(String stringToCheck) {
        String[] tokens = stringToCheck.split("");
        String command = tokens[1] + tokens[2];

        String movementRegEx = "^\\[{1}no|ea|we|so{1},[0-9]{1,9}\\]{1}$";
        String uuidRegEx = "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}";
        String transportRegEx = "^\\[{1}tr{1}," + uuidRegEx + "\\]{1}$";
        String entryRegEx = "^\\[{1}en{1}," + uuidRegEx + "\\]{1}$";

        if (isStringMatchingRegEx(stringToCheck, movementRegEx)) {
            Integer steps = Integer.parseInt(parseRestOfMachineCommand(tokens));
            return new MoveInstruction(command, steps);
        } else if (isStringMatchingRegEx(stringToCheck, uuidRegEx)) {
            UUID targetedId = UUID.fromString(parseRestOfMachineCommand(tokens));
            if (isStringMatchingRegEx(stringToCheck, transportRegEx)) {
                return new TransportInstruction(command, targetedId);
            } else if (isStringMatchingRegEx(stringToCheck, entryRegEx)) {
                return new EntryInstruction(command, targetedId);
            }
        }
        throw new MiningMachineException("Instruction does not match syntax");
    }

    private String parseRestOfMachineCommand(String[] tokens) {
        StringBuilder restOfCommand = new StringBuilder();
        for (int i = 4; i < tokens.length - 1; i++) {
            restOfCommand.append(tokens[i]);
        }
        return restOfCommand.toString();
    }

    private Instruction parseBarrierCreation(String inputToParse) {
        String[] splitBarrierString = inputToParse.split("-");

        return new BarrierInstruction(
                "br",
                splitBarrierString[0],
                splitBarrierString[1]
        );
    }

    public static Square parseSquareString(String squareString) {
        String squareRegEx = "^\\({1}[0-9]{1,9},[0-9]{1,9}\\)$";
        if (isStringMatchingRegEx(squareString, squareRegEx)) {
            return Square.createSquareFromString(squareString);
        }
        throw new MiningMachineException("Invalid Square Syntax");
    }
}
