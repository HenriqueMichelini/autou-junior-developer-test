import TextField from "./text-field/TextField";
import SubmissionHistory from "./submission-history/SubmissionHistory";
import ScrollableContainer from "./submission-history/components/ScrollableContainer";

// Sample data - you can replace this with real data later
const sampleSubmissions = [
  {
    id: "1",
    name: "contract.pdf",
    size: "850 KB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 14, 30),
  },
  {
    id: "2",
    name: "Email.txt",
    size: "45 KB",
    type: "txt" as const,
    createdAt: new Date(2026, 1, 3, 14, 35),
  },
  {
    id: "3",
    name: "Lorem ipsum, bla bla...",
    size: "850 characters",
    type: "plain" as const,
    createdAt: new Date(2026, 1, 3, 15, 45),
  },
  {
    id: "4",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
  {
    id: "5",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
  {
    id: "6",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
  {
    id: "7",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
  {
    id: "8",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
  {
    id: "9",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
  {
    id: "10",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
  {
    id: "11",
    name: "Lorem ipsum, bla bla...",
    size: "850 characters",
    type: "plain" as const,
    createdAt: new Date(2026, 1, 3, 15, 45),
  },
  {
    id: "12",
    name: "Lorem ipsum, bla bla...",
    size: "850 characters",
    type: "plain" as const,
    createdAt: new Date(2026, 1, 3, 15, 45),
  },
  {
    id: "13",
    name: "Lorem ipsum, bla bla...",
    size: "850 characters",
    type: "plain" as const,
    createdAt: new Date(2026, 1, 3, 15, 45),
  },
  {
    id: "14",
    name: "Lorem ipsum, bla bla...",
    size: "850 characters",
    type: "plain" as const,
    createdAt: new Date(2026, 1, 3, 15, 45),
  },
];

function FormSection() {
  return (
    <div className="bg-white w-[50%] h-full rounded-md shadow-lg shadow-black/15 ring-1 ring-black/5 p-4 flex flex-col gap-4">
      <ScrollableContainer>
        <SubmissionHistory submissions={sampleSubmissions} />
      </ScrollableContainer>
      <TextField />
    </div>
  );
}

export default FormSection;
