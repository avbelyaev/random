import csv
import os

GAME_RESULTS = {
    "1) Turkey vs Italy": "0-3",
    "2) Wales vs Switzerland": "1-1",
    "3) Denmark vs Finland": "0-1",
    "4) Belgium vs Russia": "3-0",
    "5) England vs Croatia": "1-0",
    "6) Austria vs North Macedonia": "3-1",
    "7) Netherlands vs Ukraine": "",
    "8) Scotland vs Czech Republic": "",
    "9) Poland vs Slovakia": "",
    "10) Spain vs Sweden": "",
}

BETS_DIR = "bets"
PLAYER_INDEX = 1
GAMES_START_FROM_INDEX = 2

EXACT_SCORE_PREDICTED_POINTS = 3
GOAL_DIFFERENCE_PREDICTED_POINTS = 2
OUTCOME_PREDICTED_POINTS = 1
WRONG_PREDICTION_POINTS = 0

# How we count points:
# - exact score: 3 pts
# - goal difference: 2pts
# - outcome (win or lose): 1pt
#
# Example:
# Final result of the game: 1-0
# - you voted 1-0 -> 3 points (exact score, goal diff, outcome - everything is correct)
# - you voted 2-1 -> 2 points (goal difference and outcome are correct)
# - you voted 3-1 -> 1 pint (outcome is correct)
# - you voted 1-1 -> 0

signum = lambda x: -1 if x < 0 else (1 if x > 0 else 0)


def count_points(expected: str, actual: str):
    actual = actual.strip()
    expected = expected.strip()
    assert actual != "" and expected != ""

    if actual == expected:
        return EXACT_SCORE_PREDICTED_POINTS

    left_expected, right_expected = list(map(lambda x: int(x), expected.split('-')))
    goal_diff_expected = left_expected - right_expected

    left_actual, right_actual = list(map(lambda x: int(x), actual.split('-')))
    goal_diff_actual = left_actual - right_actual

    if goal_diff_expected == goal_diff_actual:
        return GOAL_DIFFERENCE_PREDICTED_POINTS

    if signum(goal_diff_expected) == signum(goal_diff_actual):
        return OUTCOME_PREDICTED_POINTS

    return WRONG_PREDICTION_POINTS


def main():
    game_index = dict()
    player_points = dict()

    for root, dirs, files in os.walk(BETS_DIR):
        for file in files:
            with open(os.path.join(root, file)) as f:
                reader = csv.reader(f, delimiter=',', quotechar='"')

                is_header = True
                for row in reader:
                    if is_header:
                        is_header = False
                        for i, column in enumerate(row):
                            if i >= GAMES_START_FROM_INDEX:
                                if column in game_index:
                                    raise ValueError(f"game '{column}' already exists!")
                                game_index[column] = i
                        continue

                    player = row[PLAYER_INDEX]
                    if player not in player_points:
                        player_points[player] = 0

                    for game, index in game_index.items():
                        predicted_result = row[index]
                        actual_result = GAME_RESULTS[game]
                        if actual_result == "":
                            print(f"No results for game '{game}'. Skipping")
                            continue

                        points = count_points(predicted_result, actual_result)

                        player_points[player] += points

    # sort dict by value
    player_points_sorted = dict(sorted(player_points.items(), key=lambda item: item[1], reverse=True))

    # save as csv
    with open('player-points.csv', 'w') as f:
        writer = csv.writer(f, delimiter=',')

        writer.writerow(['Player', 'Points'])
        for player, points in player_points_sorted.items():
            print(f'{player} \t -> {points}')
            writer.writerow([player, points])


if __name__ == '__main__':
    assert EXACT_SCORE_PREDICTED_POINTS == count_points(expected='1-1', actual='1-1')
    assert EXACT_SCORE_PREDICTED_POINTS == count_points(expected='1-3', actual='1-3')
    assert EXACT_SCORE_PREDICTED_POINTS == count_points(expected='3-1', actual='3-1')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='1-3', actual='0-2')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='1-3', actual='3-5')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-1', actual='2-0')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-1', actual='4-2')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-3', actual='0-0')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-3', actual='1-1')
    assert OUTCOME_PREDICTED_POINTS == count_points(expected='3-1', actual='1-0')
    assert OUTCOME_PREDICTED_POINTS == count_points(expected='3-1', actual='4-0')
    assert OUTCOME_PREDICTED_POINTS == count_points(expected='1-3', actual='4-5')
    assert WRONG_PREDICTION_POINTS == count_points(expected='1-3', actual='3-1')
    assert WRONG_PREDICTION_POINTS == count_points(expected='1-3', actual='1-1')
    assert WRONG_PREDICTION_POINTS == count_points(expected='3-1', actual='1-1')
    assert WRONG_PREDICTION_POINTS == count_points(expected='3-3', actual='2-3')
    assert WRONG_PREDICTION_POINTS == count_points(expected='3-3', actual='3-2')
    main()
