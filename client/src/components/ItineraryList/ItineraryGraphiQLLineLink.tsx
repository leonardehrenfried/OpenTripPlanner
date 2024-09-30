import { lineQueryAsString } from '../../static/query/lineQuery.tsx';
import { Leg } from '../../gql/graphql.ts';
import { getColorForLeg } from '../../util/getColorForLeg.ts';
import { generateTextColor } from '../../util/generateTextColor.ts';

const graphiQLUrl = import.meta.env.VITE_GRAPHIQL_URL;

export function ItineraryGraphiQLLineLink({ leg }: { leg: Leg }) {
  const queryID = { id: leg.line?.id };
  const formattedQuery = encodeURIComponent(lineQueryAsString);
  const formattedQueryID = encodeURIComponent(JSON.stringify(queryID));

  const legColor = getColorForLeg(leg);
  return (
    <a
      href={graphiQLUrl + '&query=' + formattedQuery + '&variables=' + formattedQueryID}
      target={'_blank'}
      rel={'noreferrer'}
      title={`Line ${leg.line?.publicCode} to ${leg.toEstimatedCall?.destinationDisplay?.frontText}`}
      style={{
        backgroundColor: legColor,
        color: generateTextColor(legColor),
        borderRadius: '7px',
        padding: '3px 6px',
        textDecoration: 'none',
        maxWidth: '14pt',
        overflow: 'elipsis',
      }}
    >
      {leg.line?.publicCode}
    </a>
  );
}
